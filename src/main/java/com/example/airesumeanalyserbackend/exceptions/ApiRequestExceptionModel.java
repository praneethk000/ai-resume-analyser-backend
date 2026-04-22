package com.example.airesumeanalyserbackend.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiRequestExceptionModel {

    private String message;
    private HttpStatus httpStatus;
    private Throwable throwable;

}
