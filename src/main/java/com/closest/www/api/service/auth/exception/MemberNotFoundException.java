package com.closest.www.api.service.auth.exception;

import com.closest.www.api.AbstractException;
import org.springframework.http.HttpStatus;

import static com.closest.www.api.service.auth.exception.AuthServiceExceptionMessageConstants.MEMBER_NOT_FOUND;

/**
 * 존재하지 않는 회원 예외 클래스
 */
public class MemberNotFoundException extends AbstractException {

    public MemberNotFoundException() {
        super(MEMBER_NOT_FOUND);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}

