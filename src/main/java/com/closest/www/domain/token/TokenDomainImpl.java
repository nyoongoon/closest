package com.closest.www.domain.token;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 토큰 도메인 서비스 구현 클래스
 */
@Service
public class TokenDomainImpl implements TokenDomain {
    private final TokenRepository tokenRepository;

    public TokenDomainImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public void renewalToken(Token token) {
        String userEmail = token.getUserEmail();
        boolean isExists = tokenRepository.findByUserEmail(userEmail).isPresent();
        if (isExists) { // userEmail으로 등록된 리프레시 토큰 존재하면 삭제
            tokenRepository.deleteByUserEmail(userEmail);
        }
        // 새로 생성
        tokenRepository.save(token);
    }
}
