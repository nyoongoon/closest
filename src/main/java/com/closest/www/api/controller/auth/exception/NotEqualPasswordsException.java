package com.closest.www.api.controller.auth.exception;

import com.closest.www.api.AbstractException;
import org.springframework.http.HttpStatus;

import static com.closest.www.api.controller.auth.exception.AuthControllerExceptionMessageConstants.NOT_EQUAL_PASSWORDS;

public class NotEqualPasswordsException extends AbstractException {
    public NotEqualPasswordsException() {
        super(NOT_EQUAL_PASSWORDS);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
