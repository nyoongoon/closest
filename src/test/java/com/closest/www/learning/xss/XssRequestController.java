package com.closest.www.learning.xss;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class XssRequestController {

    // multipart/form-data 예시
    @PostMapping(value = "/api/multipart")
    public ResponseEntity<Map<String, String>> handleMultipartFormData(
            @RequestParam Map<String, String> formData,
            @RequestParam("file") MultipartFile file) {
        // formData는 자동으로 XSS 이스케이프 처리가 됩니다.
        Map<String, String> response = new HashMap<>();
        response.put("escapedFormData", formData.toString());

        // 파일 처리 로직 (file은 이스케이프 처리가 필요 없음)

        return ResponseEntity.ok(response);
    }

    // application/x-www-form-urlencoded 예시
    @PostMapping(value = "/api/form-urlencoded")
    public ResponseEntity<Map<String, String>> handleFormUrlEncoded(
            @RequestParam Map<String, String> formData) {
        // formData는 자동으로 XSS 이스케이프 처리가 됩니다.
        Map<String, String> response = new HashMap<>();
        response.put("escapedFormData", formData.toString());
        return ResponseEntity.ok(response);
    }


    // text/plain 예시
    @PostMapping(value = "/api/text")
    public ResponseEntity<String> handlePlainText(@RequestBody String text) {
        // text는 자동으로 XSS 이스케이프 처리가 됩니다.
        return ResponseEntity.ok("Escaped text: " + text);
    }

    // application/json 예시
    @PostMapping(value = "/api/json")
    public ResponseEntity<Map<String, Object>> handleJson(@RequestBody Map<String, Object> jsonData) {
        // jsonData는 자동으로 XSS 이스케이프 처리가 됩니다.
        Map<String, Object> response = new HashMap<>();
        response.put("escapedJsonData", jsonData);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/xss")
    public XssRequestDto postXss(@RequestBody XssRequestDto xssRequestDto) {
        return xssRequestDto;
    }

//    @PostMapping("/xss/form")
//    public String form(String content) {
//        return content;  // 실제 이스케이핑 처리는 컨버터에서 이루어짐
//    }

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
        return ResponseEntity.ok(content);
    }
}
