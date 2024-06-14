package kr.co.tetrips.user.common.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import kr.co.tetrips.user.common.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.tetrips.user.token.TokenRepository;
import kr.co.tetrips.user.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomRequestFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
  @Override
  protected void doFilterInternal(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable FilterChain filterChain) throws ServletException, IOException {
    try {
      String requestURI = Objects.requireNonNull(request).getRequestURI();
      if (requestURI.startsWith("/swagger-ui/") || requestURI.startsWith("/v3/api-docs/") || requestURI.startsWith("/api/user/login") || requestURI.startsWith("/api/user/signup")) {
        filterChain.doFilter(request, response);
        return;
      }else {
      String token = jwtProvider.extractTokenFromHeader(request);
      String subject = jwtProvider.getPayload(token).getSubject();
      if (token != null && jwtProvider.validateToken(token, subject)) {
        String email = jwtProvider.getPayload(token).get("userEmail", String.class);
        if(!jwtProvider.checkExpiration(token)){
          try {
            String refreshtoken = tokenRepository.getRefreshTokenById(userRepository.getUserIdByEmail(email));
            if (jwtProvider.checkExpiration(refreshtoken)) {
              token = jwtProvider.updateExpiration(token);
              Objects.requireNonNull(response).setHeader("Updated-Token", token);
              //암호화된 통신이 아닐 경우 헤더에 토큰을 포함시키는것은 위험할 수 있음
              //토큰을 헤더에 포함시키려면 적절한 CORS 설정이 필요함
              //서버나 클라이언트 로그에 HTTP 헤더가 기록될 수 있음
            }//리프레쉬 토큰까지 만료된 경우 로그아웃 처리하고 재 로그인이 필요하다고 리턴해줘야 함(할일)
          } catch (ExpiredJwtException e) {
              // 리프레쉬 토큰이 만료된 경우
            //jwt exception 발생 (수정필요)
              System.out.println("Refresh token expired");
              Objects.requireNonNull(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Refresh token expired. Please log in again.");

            } catch (Exception e) {
            System.out.println("이거문제인듯?");
            Objects.requireNonNull(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
          }
        }
          UserDetails userDetails = userDetailsService.loadUserByUsername(email);
          if (userDetails != null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            log.info("Authenticated user with email : {}", email);
            SecurityContextHolder.getContext().setAuthentication(authentication);
          }

      }}
      filterChain.doFilter(request, response);
    }catch (Exception e){
      log.error("doFilterInternal Error occurred while filtering request : {}", e.getMessage());
      Objects.requireNonNull(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }
  }
}
