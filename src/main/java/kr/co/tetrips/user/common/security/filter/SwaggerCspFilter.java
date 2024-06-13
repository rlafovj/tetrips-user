//package com.tetrips.api.common.security.filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class SwaggerCspFilter implements Filter {
//  @Override
//  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//    HttpServletRequest request = (HttpServletRequest) servletRequest;
//    HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//    if (request.getRequestURI().startsWith("/swagger-ui/")) {
//      response.setHeader("Content-Security-Policy", "default-src 'self'; script-src 'self'; img-src 'self' data:; font-src 'self' data:; frame-src 'self'");
//    }
//
//    filterChain.doFilter(request, response);
//  }
//}