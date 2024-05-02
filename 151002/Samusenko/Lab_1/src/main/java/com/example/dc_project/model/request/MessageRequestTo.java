package com.example.dc_project.model.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
public record MessageRequestTo(
        Long id,
        Long issueId,
        @NotBlank
        @Length(min = 2, max = 2048)
        String content
) {
}
