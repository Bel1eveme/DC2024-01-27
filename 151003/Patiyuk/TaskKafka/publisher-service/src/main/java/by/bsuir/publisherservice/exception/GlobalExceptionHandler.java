package by.bsuir.publisherservice.exception;

import by.bsuir.publisherservice.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CreateEntityException.class)
    public ResponseEntity<ErrorResponse> handleCreateEntityException(CreateEntityException e) {
        return buildDefaultResponse(e, UNPROCESSABLE_ENTITY, 1);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        return buildDefaultResponse(e, NOT_FOUND, 1);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        return buildDefaultResponse(e, BAD_REQUEST, 2);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return buildDefaultResponse(e, FORBIDDEN, 3);
    }

    @ExceptionHandler(ExchangeTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleExchangeTimeoutException(ExchangeTimeoutException e) {
        return buildDefaultResponse(e, REQUEST_TIMEOUT, 1);
    }

    @ExceptionHandler(ExchangeFailedException.class)
    public ResponseEntity<ErrorResponse> handleExchangeFailedException(ExchangeFailedException e) {
        return buildDefaultResponse(e, INTERNAL_SERVER_ERROR, 1);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return buildDefaultResponse(e, INTERNAL_SERVER_ERROR, 2);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .message(errors.toString()
                                .replace("{", "")
                                .replace("}", "")
                                .replace("=", ": "))
                        .code(BAD_REQUEST, 1)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    private ResponseEntity<ErrorResponse> buildDefaultResponse(Exception e, HttpStatus httpStatus, Integer subCode) {
        return ResponseEntity
                .status(httpStatus)
                .body(ErrorResponse.builder()
                        .message(e.getMessage())
                        .code(httpStatus, subCode)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
