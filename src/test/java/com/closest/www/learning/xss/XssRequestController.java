package com.closest.www.learning.xss;


import org.springframework.web.bind.annotation.*;

@RestController
public class XssRequestController {


    @PostMapping("/xss")
    public XssRequestDto postXss (@RequestBody XssRequestDto xssRequestDto) {
        return xssRequestDto;
    }

    @PostMapping("/xss/form")
    public String form (XssRequestDto xssRequestDto) {
        return xssRequestDto.getContent();
    }

    @PostMapping("/xss/local-date")
    public @ResponseBody XssRequestLocalDateDto xss2 (@RequestBody XssRequestLocalDateDto requestDto) {
        return requestDto;
    }

    @GetMapping("/xss")
    public XssRequestDto getXss (@ModelAttribute XssRequestDto xssRequestDto) {
        return xssRequestDto;
    }
}