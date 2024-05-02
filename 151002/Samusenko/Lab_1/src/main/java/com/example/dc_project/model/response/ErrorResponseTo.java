package com.example.dc_project.model.response;

public record ErrorResponseTo(
        int code,
        String message,
        String[] errMessages
) {
}
