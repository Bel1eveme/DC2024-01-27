package com.example.rv.api.exception;

import lombok.*;

@AllArgsConstructor
@Data
public class ApiError {
    private String errorMessage;
    private Integer errorCode;
}
