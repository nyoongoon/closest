package com.closest.www.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@ControllerAdvice
public class ControllerExceptionAdvice {

    // @ResponseStatus(HttpStatus.BAD_REQUEST) -> ResponseEntity 사용하면서 삭제
    // -> HttpStatus를 에러 클래스에서 정의한 것으로 담기 위해 ResponseEntitiy 사용함
    @ExceptionHandler({AbstractException.class})
    public ResponseEntity<ApiResponse> authException(AbstractException e) {

        ApiResponse body = ApiResponse.error(
                e.getHttpStatus(),
                e.getMessage(),
                e.getValidation()
        );

        return ResponseEntity.status(e.getHttpStatus())
                .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<String> bindException(MethodArgumentNotValidException e) {

        ApiResponse errorResponse = ApiResponse.error(
                (HttpStatus) e.getStatusCode(),
                // 첫 번째 에러의 메시지 꺼내기..
                "잘못된 요청입니다."
        );

        for (FieldError fieldError : e.getFieldErrors()) {
            errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return errorResponse;
    }
}
