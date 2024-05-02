package com.example.dc_project.model.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LabelRequestTo(
        Long id,
        @NotBlank
        @Length(min = 2, max = 32)
        String name
) {
}
