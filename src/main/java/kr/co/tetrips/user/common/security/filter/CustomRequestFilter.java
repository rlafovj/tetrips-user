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
      if (requestURI.startsWith("/swagger-ui/") || requestURI.startsWith("/v3/api-docs/") || requestURI.startsWith("/api/user/login") || requestURI.startsWith("/api/user/signup") || requestURI.startsWith("/api/security/renew")) {
        filterChain.doFilter(request, response);
        return;
      }else {
      String token = jwtProvider.extractTokenFromHeader(request);
      String subject = "access";
      if (token != null && jwtProvider.validateToken(token, subject)) {
        String email = jwtProvider.getPayload(token).get("userEmail", String.class);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (userDetails != null) {
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          log.info("Authenticated user with email : {}", email);
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    }
      filterChain.doFilter(request, response);
    }catch (Exception e){
      log.error("doFilterInternal Error occurred while filtering request : {}", e.getMessage());
      Objects.requireNonNull(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token expired. Please refresh request." + e.getMessage());
    }
  }
}
