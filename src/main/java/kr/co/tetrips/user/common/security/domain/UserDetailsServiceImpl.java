package com.tetrips.api.common.security.domain;

import com.tetrips.api.user.User;
import com.tetrips.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<User> user = Optional.ofNullable(userRepository.findUserByEmail(email)).orElseThrow(()->new UsernameNotFoundException("User Not Found with email: " + email));
    return UserDetailsImpl.build(user.get());
  }
}
