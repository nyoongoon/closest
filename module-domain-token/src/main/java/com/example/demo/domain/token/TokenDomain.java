package com.example.demo.domain.token;

import com.example.demo.domain.token.entity.Token;

/**
 * 토큰 도메인 서비스 인터페이스
 */
public interface TokenDomain {
    void renewalToken(Token token);
}
