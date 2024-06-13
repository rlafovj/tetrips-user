package com.tetrips.api.user;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserQueryDSL {
  Optional<User> findUserByEmail(String email);
  boolean existsByEmail(String email);
}
