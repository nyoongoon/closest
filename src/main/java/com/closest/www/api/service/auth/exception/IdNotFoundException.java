package com.closest.www.api.service.auth.exception;

import com.closest.www.api.AbstractException;
import org.springframework.http.HttpStatus;

/**
 * 존재하지 않는 아이디 예외 클래스
 */
public class IdNotFoundException extends AbstractException {
    private static final String MESSAGE = "존재하지 않는 아이디입니다.";

    public IdNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}

