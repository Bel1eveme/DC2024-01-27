package com.poluectov.reproject.discussion.repository.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ValidationError {

    private String field;
    private String error;
}
