package org.example.publisher.api.exception;

import lombok.*;

@AllArgsConstructor
@Data
public class ApiError {
    private String errorMessage;
    private Integer errorCode;
}
