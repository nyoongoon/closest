package com.closest.www.learning.xss;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class XssRequestLocalDateDto {
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate requestDate;

    public XssRequestLocalDateDto() {
    }

    public XssRequestLocalDateDto(String content, LocalDate requestDate) {
        this.content = content;
        this.requestDate = requestDate;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }
}