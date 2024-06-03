package com.closest.www.domain.token;

import com.closest.www.domain.token.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 토큰 리포지토리
 */
interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUserEmail(String userEmail);
    void deleteByUserEmail(String userEmail);
}
