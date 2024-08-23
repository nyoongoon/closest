package com.closest.www.learning.xss;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class XssTestController {

    // multipart/form-data 예시 - 문자열
    @PostMapping(value = "/api/multipart")
    public ResponseEntity<Map<String, String>> handleMultipartFormData(
            @RequestParam("file") MultipartFile file,
            @RequestParam("content") String content) {

        Map<String, String> response = new HashMap<>();
        response.put("escapedFormData", "{field=" + content + "}");

        return ResponseEntity.ok(response);
    }

    // multipart/form-data 예시 - 객체
    @PostMapping(value = "/api/multipart/object")
    public ResponseEntity<Map<String, String>> handleMultipartFormDataObj(
            @RequestParam("file") MultipartFile file,
            @RequestParam("content") XssRequestDto xssRequestDto) {

        Map<String, String> response = new HashMap<>();
        response.put("escapedFormData", "{field=" + xssRequestDto.getContent() + "}");

        return ResponseEntity.ok(response);
    }

    // multipart/form-data 예시 - 문자열 & @RequestPart
    @PostMapping(value = "/api/multipart/part")
    public ResponseEntity<Map<String, String>> handleMultipartFormDataByRequestPart(
            @RequestPart("file") MultipartFile file,
            @RequestPart("content") String content) {

        Map<String, String> response = new HashMap<>();
        response.put("escapedFormData", "{field=" + content + "}");

        return ResponseEntity.ok(response);
    }

    // multipart/form-data 예시 - 객체& @RequestPart
    @PostMapping(value = "/api/multipart/object/part")
    public ResponseEntity<Map<String, String>> handleMultipartFormDataObjByRequestPart(
            @RequestPart("file") MultipartFile file,
            @RequestPart("content") XssRequestDto xssRequestDto) {

        Map<String, String> response = new HashMap<>();
        response.put("escapedFormData", "{field=" + xssRequestDto.getContent() + "}");

        return ResponseEntity.ok(response);
    }

    // application/x-www-form-urlencoded 예시
    @PostMapping(value = "/api/form-urlencoded")
    public ResponseEntity<XssRequestDto> handleFormUrlEncoded(@RequestBody MultiValueMap<String, String> formData) {
        // formData는 자동으로 XSS 이스케이프 처리가 됩니다.
        return ResponseEntity.ok(new XssRequestDto(formData.get("field").getFirst()));
    }

    // application/x-www-form-urlencoded 예시
    @PostMapping(value = "/api/form-urlencoded/param")
    public ResponseEntity<XssRequestDto> handleFormUrlEncoded(@RequestParam("field") String field) {
        // formData는 자동으로 XSS 이스케이프 처리가 됩니다.
        return ResponseEntity.ok(new XssRequestDto(field));
    }

    // text/plain 예시
    @PostMapping(value = "/api/text")
    public ResponseEntity<String> handlePlainText(@RequestBody String text) {

        return ResponseEntity.ok("Escaped text: " + text);
    }

    // application/json 예시
    @PostMapping(value = "/api/json")
    public ResponseEntity<XssRequestDto> handleJson(@RequestBody XssRequestDto dto) {
        // jsonData는 자동으로 XSS 이스케이프 처리가 됩니다.
        return ResponseEntity.ok(dto);
    }

    /**
     * 예외 예시 컨트롤러
     * <p>
     * 해당컨트롤러는 @RequestBody를 선언하지 않았으므로 스프링에서 자동적으로 @ModelAttribute로서 파라미터를 받는다.
     *
     * @ModelAttribute는 바디가 아닌 파라미터로 값을 전달하는 것이므로 POST요청에 적합하지 않는다.
     */
    @PostMapping("/xss/form")
    public String form(XssRequestDto xssRequestDto) {
        return xssRequestDto.getContent();
    }

    @PostMapping("/multipart")
    public byte[] handleMultipartFile(@RequestParam("file") MultipartFile file) throws IOException {
        // 현재는 파일 내용을 그대로 반환
        return file.getBytes();
    }

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
