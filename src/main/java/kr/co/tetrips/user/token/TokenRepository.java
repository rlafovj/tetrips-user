package com.tetrips.api.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long>, TokenQueryDSL {
  void deleteById(Long id);
}
