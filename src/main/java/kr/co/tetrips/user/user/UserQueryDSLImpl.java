package kr.co.tetrips.user.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserQueryDSLImpl implements UserQueryDSL{
  private final JPAQueryFactory factory;

  @Override
  public Optional<User> findUserByEmail(String email) {
    QUser qUser = QUser.user;
    return Optional.ofNullable(factory.selectFrom(qUser)
            .where(qUser.email.eq(email))
            .fetchFirst());
  }

  @Override
  public boolean existsByEmail(String email) {
    QUser qUser = QUser.user;
    return factory.selectFrom(QUser.user)
            .where(qUser.email.eq(email))
            .fetchFirst() != null;
  }
}
