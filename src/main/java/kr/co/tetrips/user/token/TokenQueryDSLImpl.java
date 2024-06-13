package com.tetrips.api.token;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class TokenQueryDSLImpl implements TokenQueryDSL{
  private final JPAQueryFactory factory;
  private final QToken qToken = QToken.token;
  @Override
  public Optional<Token> findTokenByRefreshToken(String token) {
    return Optional.ofNullable(factory.selectFrom(qToken)
            .where(qToken.refreshToken.eq(token))
            .fetchFirst());
  }

  @Override
  public Boolean existsByRefreshToken(String token) {
    return factory.selectFrom(qToken)
            .where(qToken.refreshToken.eq(token)).fetchFirst() != null;
  }
}
