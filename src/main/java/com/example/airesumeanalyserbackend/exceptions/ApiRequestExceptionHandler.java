package com.example.airesumeanalyserbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ApiRequestExceptionHandler {
    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<ApiRequestExceptionModel> handleApiRequestException(ApiRequestException exception) {
        ApiRequestExceptionModel apiRequestExceptionModel = new ApiRequestExceptionModel(exception.getMessage(), HttpStatus.BAD_REQUEST, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiRequestExceptionModel);
    }
}

