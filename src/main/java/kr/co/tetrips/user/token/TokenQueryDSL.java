package com.tetrips.api.token;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenQueryDSL {
  Optional<Token> findTokenByRefreshToken(String token);
  Boolean existsByRefreshToken(String token);
}
