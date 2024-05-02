package com.example.dc_project.service.exceptions;

import com.example.dc_project.model.response.ErrorResponseTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerException {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseTo> catchNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorResponseTo());
    }

    @ExceptionHandler(ResourceStateException.class)
    public ResponseEntity<ErrorResponseTo> catchStateException(ResourceStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getErrorResponseTo());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseTo> catchValidationException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(takeError(e));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponseTo> catchThrowable(Throwable e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(takeError(e));
    }

    private static ErrorResponseTo takeError(MethodArgumentNotValidException e) {
        List<String> messages = new ArrayList<>();

        for (ObjectError objectError : e.getAllErrors()) {
            if (objectError instanceof FieldError fieldError) {
                messages.add("FieldError " + fieldError.getField() + ": " + fieldError.getDefaultMessage());
            } else {
                messages.add(objectError.toString());
            }
        }

        return new ErrorResponseTo(
                HttpStatus.BAD_REQUEST.value() * 100 + 5,
                "Some parameters are not correct",
                messages.toArray(String[]::new)
        );
    }

    private static ErrorResponseTo takeError(Throwable e) {
        return new ErrorResponseTo(
                HttpStatus.BAD_REQUEST.value() * 100 + 6,
                "Special error caught in the request",
                new String[] {e.getMessage()}
        );
    }
}