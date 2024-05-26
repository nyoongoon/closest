package com.example.closest.auth.configuration;

import com.example.closest.common.exception.auth.TokenExpiredException;
import com.example.closest.utility.token.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Jwt 필터 클래스
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { //OncePerRequestFilter -> 한 요청당 한번 필터 실행..
    private final TokenProvider tokenProvider;

    public JwtAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // 요청 -> filter -> servlet -> interceptor -> aop -> controller
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = this.tokenProvider.resolveAccessTokenByHeader(request);
        String refreshToken = this.tokenProvider.resolveRefreshTokenByCookie(request);
        if (accessToken == null && refreshToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isAccessTokenValidate = this.tokenProvider.validateAccessToken(accessToken);
        boolean isRefreshTokenValidate = this.tokenProvider.validateRefreshToken(refreshToken);

        if (isAccessTokenValidate) {
            // 엑세스 토큰 유효
            this.setAuthenticationByAccessToken(accessToken);
        } else if (isRefreshTokenValidate) {
            // 리프레시 토큰 유효
            accessToken = this.tokenProvider.getAccessTokenByRefreshToken(refreshToken);
            this.setAuthenticationByAccessToken(accessToken);
            this.tokenProvider.addAccessTokenToCookie(response, accessToken);
        } else {
            throw new TokenExpiredException();
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationByAccessToken(String accessToken) {
        Authentication auth = this.tokenProvider.getAuthentication(accessToken); // 인증 정보 가져오기
        SecurityContextHolder.getContext().setAuthentication(auth); // 시큐리티 컨텍스트에 인증 정보 담기
    }
}
