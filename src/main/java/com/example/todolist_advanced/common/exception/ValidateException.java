package com.example.todolist_advanced.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ValidateException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;

    public ValidateException(String message, HttpStatus httpStatus) {
        this.message=message;
        this.httpStatus=httpStatus;
    }
}
