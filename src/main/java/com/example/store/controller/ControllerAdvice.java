package com.example.store.controller;

import com.example.store.dtos.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<ErrorResponse> illegalStateExceptionHandler(IllegalStateException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> expiredJwtExceptionHandler(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
    }
}
