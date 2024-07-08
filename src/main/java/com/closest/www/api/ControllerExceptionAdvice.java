package com.closest.www.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerExceptionAdvice {
    @ExceptionHandler({AbstractException.class})
    public ResponseEntity<ErrorResponse> authException(AbstractException e) {

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

        return response;
    }
}
