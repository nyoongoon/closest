package com.closest.www.api.service.auth.exception;

import com.closest.www.api.AbstractException;
import org.springframework.http.HttpStatus;

import static com.closest.www.api.controller.auth.exception.AuthControllerExceptionMessageConstants.NOT_EQUAL_PASSWORDS;

/**
 * 비빌번호, 확인 비밀번호 일치 판단 예외
 * - DTO에서 각 값을 꺼내 판단해야하므로 서비스 예외로 관리한다.
 */
public class NotEqualPasswordsException extends AbstractException {
    public NotEqualPasswordsException() {
        super(NOT_EQUAL_PASSWORDS);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
