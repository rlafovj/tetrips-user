//package kr.co.tetrips.user.common.security.service;
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthenticationService {
//  private final AuthenticationManager authenticationManager;
//
//  public AuthenticationService(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//    this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
//  }
//
//  public Authentication authenticate(Authentication authentication) {
//    return authenticationManager.authenticate(authentication);
//  }
//}
