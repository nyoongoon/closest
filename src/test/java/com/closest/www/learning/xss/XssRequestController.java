package com.closest.www.learning.xss;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class XssRequestController {

    @PostMapping("/xss")
    public XssRequestDto postXss(@RequestBody XssRequestDto xssRequestDto) {
        return xssRequestDto;
    }

    @PostMapping("/xss/form")
    public String form(XssRequestDto xssRequestDto) {
        return xssRequestDto.getContent();
    }

//    @PostMapping("/multipart")
//    public byte[] handleMultipartFile(@RequestParam("file") MultipartFile file) throws IOException {
//        // 현재는 파일 내용을 그대로 반환
//        return file.getBytes();
//    }

    @PostMapping("/upload")
    public String handleUpload(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute FormData formData) throws IOException {
        // 파일과 formData를 사용한 처리
        return formData.getContent();
    }

    @PostMapping("/xss/local-date")
    public @ResponseBody XssRequestLocalDateDto xss2(@RequestBody XssRequestLocalDateDto requestDto) {
        return requestDto;
    }

    @GetMapping("/xss")
    public XssRequestDto getXss(@ModelAttribute XssRequestDto xssRequestDto) {
        return xssRequestDto;
    }

    // 없는 URL을 위한 메서드
    @RequestMapping("/invalid-url")
    public ResponseEntity<String> handleInvalidUrl() {
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/plain")
    public ResponseEntity<String> plainTextXssProtection(@RequestBody String content) {
        // HTML escape the content
//        String escapedContent = StringEscapeUtils.escapeHtml4(content);
//        return ResponseEntity.ok(escapedContent);
        return ResponseEntity.ok(content);
    }
}
