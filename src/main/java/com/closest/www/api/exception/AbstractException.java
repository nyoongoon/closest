package com.closest.www.api.exception;


import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractException extends RuntimeException{
    private final Map<String, String> validation = new HashMap<>();

    public AbstractException(String message) {
        super(message);
    }

    public Map<String, String> getValidation() {
        return validation;
    }

    public abstract HttpStatus getHttpStatus();

    public void addValidation(String fieldName, String message){
        this.validation.put(fieldName, message);
    }
}
