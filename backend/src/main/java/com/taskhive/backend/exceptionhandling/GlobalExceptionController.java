package com.taskhive.backend.exceptionhandling;

import com.taskhive.backend.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//this is just for test purposes, remove this safely
@ControllerAdvice
@Slf4j
public class GlobalExceptionController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> globalExceptionHandler(Exception ex) {
        log.info("exception caught by GloablExceptoinController: " + ex.getMessage());
        Response response = new Response();
        return ResponseEntity.badRequest().body(response);
    }


    //all the validations exceptions are handled at one place

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationException(MethodArgumentNotValidException ex) {
        Response response = new Response();
        response.setMessage(ex.getMessage());
        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
    }
}
