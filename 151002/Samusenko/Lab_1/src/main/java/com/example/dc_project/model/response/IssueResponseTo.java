package com.example.dc_project.model.response;

import java.time.LocalDateTime;

public record IssueResponseTo(
        Long id,
        Long userId,
        String title,
        String content,
        LocalDateTime created,
        LocalDateTime modified
) {
}
