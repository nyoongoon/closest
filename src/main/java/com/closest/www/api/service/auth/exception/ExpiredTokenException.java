package com.closest.www.api.service.auth.exception;

import com.closest.www.api.AbstractException;
import org.springframework.http.HttpStatus;

import static com.closest.www.api.service.auth.exception.AuthServiceExceptionMessageConstants.EXPIRED_TOKEN;

/**
 * 토큰 만료 예외 클래스
 */
//@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Token has expired")
public class ExpiredTokenException extends AbstractException {
    public ExpiredTokenException() {
        super(EXPIRED_TOKEN);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
