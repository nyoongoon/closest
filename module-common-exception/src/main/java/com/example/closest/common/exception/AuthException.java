package com.example.closest.common.exception;


import java.util.HashMap;
import java.util.Map;

public abstract class AuthException extends RuntimeException{
    private final Map<String, String> validation = new HashMap<>();

    public AuthException(String message) {
        super(message);
    }

    public Map<String, String> getValidation() {
        return validation;
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message){
        this.validation.put(fieldName, message);
    }
}
