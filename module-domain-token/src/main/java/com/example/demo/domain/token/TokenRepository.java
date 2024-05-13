package com.example.demo.domain.token;

import com.example.demo.domain.token.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 토큰 리포지토리
 */
interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUsername(String username);
    void deleteByUsername(String username);
}
