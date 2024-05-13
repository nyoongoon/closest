package com.example.closest.common.exception.auth;

import com.example.closest.common.exception.AuthException;
import org.springframework.http.HttpStatus;

// 이미 존재하는 아이디 예외 클래스
public class DuplicateIdException extends AuthException {
    private static final String MESSAGE = "이미 사용 중인 아이디입니다.";

    public DuplicateIdException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
