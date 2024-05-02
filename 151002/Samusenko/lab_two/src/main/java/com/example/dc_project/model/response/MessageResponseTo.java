package com.example.dc_project.model.response;

public record MessageResponseTo(
        Long id,
        Long issueId,
        String content
) {
}
