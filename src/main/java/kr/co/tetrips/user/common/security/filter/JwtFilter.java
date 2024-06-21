//package kr.co.tetrips.user.common.security.filter;
//
//
//import io.jsonwebtoken.ExpiredJwtException;
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import kr.co.tetrips.user.common.JwtProvider;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.lang.Nullable;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Objects;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class JwtFilter extends OncePerRequestFilter {
//  private final JwtProvider jwtProvider;
//  private final AuthenticationManager authenticationManager;
//
//  @Override
//  protected void doFilterInternal(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable FilterChain filterChain) throws ServletException, IOException {
//    try {
//      String requestURI = Objects.requireNonNull(request).getRequestURI();
//      if (requestURI.startsWith("/swagger-ui/") || requestURI.startsWith("/v3/api-docs/") || requestURI.startsWith("/api/user/login") || requestURI.startsWith("/api/user/signup") || requestURI.startsWith("/api/security/renew")) {
//        filterChain.doFilter(request, response);
//      }else{
//        String token = jwtProvider.extractTokenFromHeader(request);
//        String subject = "access";
//        if (token != null && jwtProvider.validateToken(token, subject)) {
//          try{
//            if (jwtProvider.checkExpiration(token)) {
//              SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//              String email = jwtProvider.getPayload(token).get("userEmail", String.class);
//              String role = jwtProvider.getPayload(token).get("role", String.class);
//              UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, null);
//              log.info("Authenticated user with email : {}", email);
//              Authentication authentication = authenticationManager.authenticate(authenticationToken);
//              if(authentication.isAuthenticated()){
//                log.info("@@@@@@@@@@@@인증된유저임@@@@@@@@@@@@User is authenticated@@@@@@@@@@@@@@");
//              }
//              securityContext.setAuthentication(authentication);
//
//            }else {
//              throw new ExpiredJwtException(null, null, "Access token expired. Please refresh request. 다시.");
//            }
//          } catch (ExpiredJwtException e) {
//            Objects.requireNonNull(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token expired. Please refresh request. 다시2" + e.getMessage());
//          }
//        }
//      }
//      filterChain.doFilter(request, response);
//    }catch (Exception e){
//      log.error("doFilterInternal Error occurred while filtering request : {}", e.getMessage());
//      Objects.requireNonNull(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token expired. Please refresh request. 다시3" + e.getMessage());
//    }
//
//  }
//}
//
