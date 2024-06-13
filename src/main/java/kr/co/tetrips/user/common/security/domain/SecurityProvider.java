//package com.tetrips.api.common.security.domain;
//JwtProvider와 같은 역할
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.util.Base64;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class SecurityProvider implements AuthenticationProvider {
//
//  private final UserDetailsServiceImpl userDetailsServiceImpl;
//
//  @Value("${jwt.acc-exp}")
//  private Long accessExpiredDate;
//  @Value("${jwt.ref-exp}")
//  private Long refreshExpiredDate;
//  @Value("${jwt.secret}")
//  private SecretKey secretKey;
//
//  @PostConstruct
//  protected void init(){
//    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//  }
//
//  @Override
//  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//    return null;
//  }
//
//  @Override
//  public boolean supports(Class<?> authentication) {
//    return false;
//  }
//}
