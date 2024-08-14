package com.closest.www.learning.xss;


public class XssRequestDto {
    private String content;

    public XssRequestDto() {}

    public XssRequestDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
