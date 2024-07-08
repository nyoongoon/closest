package com.closest.www.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ControllerAdvice
public class ControllerExceptionAdvice {
    @ExceptionHandler({AbstractException.class})
    public ApiResponse<> authException(AbstractException e) {

        int statusCode = e.getStatusCode();

        ErrorResponse body = new ErrorResponse.Builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation()) // 밸리데이션 추가하기 -> 에러가 발생한 필드 정보 담아주는 것 필요..
                .build();

        // 응답 json validation -> title : 제목에 바보를 포함할 수 없습니다.

        //@ResponseStatus를 제거하고 ResponseEntity.status()로 헤더에 상태코드 심어주기 !
        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(body);

        ApiResponse.of(
                String.valueOf(statusCode),
                e.getMessage(),
                null
        );

        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<String> bindException(BindException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                // 첫 번째 에러의 메시지 꺼내기..
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                null
        );
    }
}
