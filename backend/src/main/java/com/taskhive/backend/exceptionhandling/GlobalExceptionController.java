package com.taskhive.backend.exceptionhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//this is just for test purposes, remove this safely
@ControllerAdvice
@Slf4j
public class GlobalExceptionController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> globalExceptionHandler(Exception ex) {
        log.error("exception caught: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(ex.getMessage());
    }
}
