package com.closest.www.api.service.auth.exception;

import com.closest.www.api.AbstractException;
import org.springframework.http.HttpStatus;

public class NotEqualPasswordsException extends AbstractException {
    private static final String MESSAGE = "비밀번호와 확인 비밀번호가 다릅니다.";

    public NotEqualPasswordsException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
