package com.project.recipe.config;

import com.project.recipe.dto.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 Bad Request (잘못된 요청)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseMessage> handleInvalidJsonRequest(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다.", null));
    }

    // 400 Bad Request (사용자 정의 예외)
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseMessage> handleCustomException(CustomException ex) {
        ResponseMessage errorResponse = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 500 Internal Server Error (서버 내부 오류)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage> handleGlobalException(Exception ex) {
        ResponseMessage errorResponse = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다.", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}