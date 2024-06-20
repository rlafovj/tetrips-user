package kr.co.tetrips.user.common.config;
import kr.co.tetrips.user.common.security.filter.CustomRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  //Flux 방식으로 마이그레이션 필요함 240612
  // 개발자가 기획에 따라 커스터마이징 해야 함
//    private final CustomRequestFilter jwtAuthenticationFilter ;
//    private final UserDetailsService customerUserDetailsService ;
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    AuthenticationManager manager = managerBuilder.build();
    http.authenticationManager(manager);

    http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(authorizeRequest ->
        authorizeRequest
          .requestMatchers(
                  AntPathRequestMatcher.antMatcher("/admin/**")
          ).hasRole("ADMIN")
          .requestMatchers(
                  AntPathRequestMatcher.antMatcher("/**"),
                  AntPathRequestMatcher.antMatcher("/error"),
                  AntPathRequestMatcher.antMatcher("/api/user/login/**"),
                  AntPathRequestMatcher.antMatcher("/api/user/signup/**"),
                  AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
                  AntPathRequestMatcher.antMatcher("/swagger-resources/**"),
                  AntPathRequestMatcher.antMatcher("/v3/api-docs/**")
          ).permitAll()
          .anyRequest().authenticated()

      )
      //    .addFilterAt(
      //            this.abstractAuthenticationProcessingFilter(manager),
      //            UsernamePasswordAuthenticationFilter.class)
      .headers(
        headersConfigurer ->
          headersConfigurer
            .frameOptions(
              HeadersConfigurer.FrameOptionsConfig::sameOrigin
            )
            .contentSecurityPolicy( policyConfig ->
              policyConfig.policyDirectives(
                    "script-src 'self'; " + "img-src 'self' data:; " +
                                  "font-src 'self' data:; " + "default-src 'self'; " +
                                      "frame-src 'self'"
              )
            )
      );

    return http.build();
  }
  public AbstractAuthenticationProcessingFilter abstractAuthenticationProcessingFilter(final AuthenticationManager manager) {
    // CustomRequestFilter filter = new CustomRequestFilter(
    //        "/api/login",
    //        manager
    // );
    CustomRequestFilter filter = null;
    return null;
  }
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
    return authenticationConfiguration.getAuthenticationManager();}
  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder(); }
}
