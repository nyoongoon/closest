package com.closest.www.domain.blog;

import com.closest.www.AbstractException;
import org.springframework.http.HttpStatus;

public class BlogNotFoundException extends AbstractException {
    private static final String MESSAGE = "존재하지 않는 블로그입니다.";

    public BlogNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}