package com.closest.www.domain.feed.exception;

import com.closest.www.api.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UrlException extends AbstractException {
    private static final String MESSAGE = "URL 변환 중 에러가 발생하였습니다.";

    public UrlException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
