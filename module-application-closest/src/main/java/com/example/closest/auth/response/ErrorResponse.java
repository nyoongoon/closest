package com.example.closest.auth.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY) //응답 내려줄 떄 비어있는 필드는 없애고 json 변환시킴!
public class ErrorResponse {
    private final String code;
    private final String message;
    private Map<String, String> validation = new HashMap<>();

    private ErrorResponse(Builder builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.validation = builder.validation;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getValidation() {
        return validation;
    }


    public static final class Builder {
        private String code;
        private String message;
        private Map<String, String> validation;

        public Builder() {
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder validation(Map<String, String> validation) {
            this.validation = validation;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }
}
