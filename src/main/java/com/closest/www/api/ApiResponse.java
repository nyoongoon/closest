package com.closest.www.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY) //응답 내려줄 떄 비어있는 필드는 없애고 json 변환시킴!
public class ApiResponse<T> {
    private int code;
    private HttpStatus status;
    private String message;
    private T data;
    private Map<String, String> validation;

    public ApiResponse(HttpStatus status, String message, T data, Map<String, String> validation) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
        this.validation = validation;
    }


    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data, null);
    }

    public static <T> ApiResponse<T> of(HttpStatus status, T data) {
        return ApiResponse.of(status, status.name(), data);
    }

    public static <T> ApiResponse<T> ok() {
        return ApiResponse.ok(null);
    }
    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.of(HttpStatus.OK, HttpStatus.OK.name(), data);
    }

    public static ApiResponse error(HttpStatus status, String message, Map<String, String> validation) {
        return new ApiResponse<>(status, message, null, validation);
    }

    public static ApiResponse error(HttpStatus status, String message) {
        return ApiResponse.error(status, message, new HashMap<>());
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Map<String, String> getValidation() {
        return validation;
    }
}
