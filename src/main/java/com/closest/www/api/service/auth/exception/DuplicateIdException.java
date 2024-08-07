package com.closest.www.api.service.auth.exception;

import com.closest.www.api.AbstractException;
import org.springframework.http.HttpStatus;

import static com.closest.www.api.service.auth.exception.AuthServiceExceptionMessageConstants.DUPLICATED_ID;

// 이미 존재하는 아이디 예외 클래스
public class DuplicateIdException extends AbstractException {

    public DuplicateIdException() {
        super(DUPLICATED_ID);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
