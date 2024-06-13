package com.tetrips.api.common.interceptor;

import com.tetrips.api.common.JwtProvider;
import com.tetrips.api.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
  private final JwtProvider jwtProvider;
  private final UserRepository userRepository;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    return Stream.of(request)
            .map(i -> jwtProvider.extractTokenFromHeader(i))
            .filter(i -> !i.equals("undefined token"))
            .peek(token -> log.info("1- 인터셉터 토큰 로그 Bearer 포함 : {}", token))
            .filter(i -> jwtProvider.validateToken(i, "access"))
            .map(i -> jwtProvider.getPayload(i).get("userEmail", String.class))
            .map(email -> userRepository.findUserByEmail(email))
            .filter(i -> i.isPresent())
            .peek(email -> log.info("2- 인터셉트사용자 Email : {}", email))
            .findFirst()
            .isPresent();

}
}
