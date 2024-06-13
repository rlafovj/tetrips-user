package kr.co.tetrips.user.common.config;

import kr.co.tetrips.user.common.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
  private final AuthInterceptor authInterceptor;

  public void addInterceptor(InterceptorRegistry registry) {
    registry.addInterceptor(authInterceptor)
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/user/signup")
            .excludePathPatterns("/api/user/login")
            .excludePathPatterns("/swagger-ui/**")
            .excludePathPatterns("/v3/api-docs/**")
            .excludePathPatterns("/favicon.ico");
  }
}
