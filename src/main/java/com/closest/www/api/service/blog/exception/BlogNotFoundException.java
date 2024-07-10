package com.closest.www.api.service.blog.exception;

import com.closest.www.api.AbstractException;
import org.springframework.http.HttpStatus;

public class BlogNotFoundException extends AbstractException {
    private static final String MESSAGE = "존재하지 않는 블로그입니다.";

    public BlogNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}