package com.example.closest.utility.token;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.closest.common.exception.Authority;
import com.example.closest.common.dto.TokenDto;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * 토큰 유틸 인터페이스
 */
public interface TokenProvider {
    // 토큰 발급
    TokenDto getTokens(String userEmail, List<Authority> roles);

    String getRefreshTokenByClaims(Claims claims);

    String getAccessTokenByRefreshToken(String refreshToken);

    // 권한 얻기
    Authentication getAuthentication(String jwt);

    boolean validateToken(String token, String secretKey);

    boolean validateAccessToken(String token);

    boolean validateRefreshToken(String token);

    void addAccessTokenToCookie(HttpServletResponse response, String refreshToken);

    void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken);

    String resolveAccessTokenByHeader(HttpServletRequest request);

    String resolveRefreshTokenByCookie(HttpServletRequest request);

}
