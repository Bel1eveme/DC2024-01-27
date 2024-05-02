package com.example.rv.api.exception.handler;

import com.example.rv.api.exception.ApiError;
import com.example.rv.api.exception.DuplicateEntityException;
import com.example.rv.api.exception.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

@ControllerAdvice
public class GlobalEntityExceptionHandler {
    @ExceptionHandler({EntityNotFoundException.class, DuplicateEntityException.class})
    public final ResponseEntity<ApiError> handleException(Exception exception, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        if (exception instanceof EntityNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            ApiError apiError = new ApiError(exception.getMessage(), HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(apiError, headers, status);
        }

        if (exception instanceof DuplicateEntityException) {
            HttpStatus status = HttpStatus.FORBIDDEN;
            ApiError apiError = new ApiError(exception.getMessage(), HttpStatus.CONFLICT.value());
            return new ResponseEntity<>(apiError, headers, status);
        }

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(exception, null, headers, status, request);
    }

    protected ResponseEntity<ApiError> handleExceptionInternal(Exception ex, ApiError body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
    }
}
