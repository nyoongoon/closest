package com.example.demo.common.exception.auth;

import com.example.demo.common.exception.AuthException;
import org.springframework.http.HttpStatus;

/**
 * 토큰 만료 예외 클래스
 */
//@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Token has expired")
public class TokenExpiredException extends AuthException {
    private static final String MESSAGE = "토큰이 만료되었습니다..";
    public TokenExpiredException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}
