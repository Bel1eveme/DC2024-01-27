package com.example.publicator.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> onConstraintValidationException(ConstraintViolationException e) {
        return new ResponseEntity<>(new Violation("Bad request", 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> onNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(new Violation(e.getMessage(), e.getStatus()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<?> onDuplicateException(DuplicateException e) {
        return new ResponseEntity<>(new Violation(e.getMessage(), e.getStatus()), HttpStatus.FORBIDDEN);
    }


}
