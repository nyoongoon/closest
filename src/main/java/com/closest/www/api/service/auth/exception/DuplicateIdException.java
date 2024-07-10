package com.closest.www.api.service.auth.exception;

import com.closest.www.api.AbstractException;
import org.springframework.http.HttpStatus;

// 이미 존재하는 아이디 예외 클래스
public class DuplicateIdException extends AbstractException {
    private static final String MESSAGE = "이미 사용 중인 아이디입니다.";

    public DuplicateIdException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
