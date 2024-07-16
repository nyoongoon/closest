package com.closest.www.domain.feed.exception;

import com.closest.www.api.AbstractException;
import org.springframework.http.HttpStatus;

/**
 * RssFeedReader 예외 클래스
 */
public class RssFeedReaderException extends AbstractException {
    private static final String MESSAGE = "RSS 피드를 읽어오는 중 에러가 발생하였습니다.";

    public RssFeedReaderException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
