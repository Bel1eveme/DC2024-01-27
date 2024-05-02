package by.rusakovich.publisher.news.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewsRequestTO(
        Long id,
        Long authorId,
        @NotBlank @Size(min = 2, max = 64) String title,
        @Size(min = 4, max = 2048) String content

) {
}
