package com.closest.www.domain.feed.exception;

import com.closest.www.api.AbstractException;
import org.springframework.http.HttpStatus;

/**
 * RssFeedReader 예외 클래스
 */
public class FeedNotFoundException extends AbstractException {
    private static final String MESSAGE = "피드를 찾을 수 없습니다.";

    public FeedNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
