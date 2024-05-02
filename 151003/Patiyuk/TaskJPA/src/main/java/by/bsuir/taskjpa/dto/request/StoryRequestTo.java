package by.bsuir.taskrest.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StoryRequestTo(
        Long id,
        Long creatorId,
        @NotNull @Size(min = 2, max = 64)
        String title,
        @NotNull @Size(min = 4, max = 2048)
        String content
) {
}
