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
    private Map<String, String> validation = new HashMap<>();

    public ApiResponse(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> of(HttpStatus status, T data) {
        return ApiResponse.of(status, status.name(), data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.of(HttpStatus.OK, HttpStatus.OK.name(), data);
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


    public static final class Builder {
        private int code;
        private HttpStatus status;
        private String message;
        private T data;
        private Map<String, String> validation;

        private Builder() {
        }

        public static Builder anApiResponse() {
            return new Builder();
        }

        public Builder withCode(int code) {
            this.code = code;
            return this;
        }

        public Builder withStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withData(T data) {
            this.data = data;
            return this;
        }

        public Builder withValidation(Map<String, String> validation) {
            this.validation = validation;
            return this;
        }

        public ApiResponse build() {
            ApiResponse apiResponse = new ApiResponse(status, message, data);
            apiResponse.validation = this.validation;
            apiResponse.code = this.code;
            return apiResponse;
        }
    }
}
