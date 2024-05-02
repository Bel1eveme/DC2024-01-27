package by.bsuir.taskrest.dto.response;

import java.time.LocalDateTime;

public record StoryResponseTo(
        Long id,
        Long creatorId,
        String title,
        String content,
        LocalDateTime created,
        LocalDateTime modified
) {
}
