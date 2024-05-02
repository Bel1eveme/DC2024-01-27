package by.bsuir.discussionservice.dto.request;

import by.bsuir.discussionservice.entity.MessageState;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MessageRequestTo(
        Long id,
        Long storyId,
        String country,
        @NotNull @Size(min = 2, max = 2048)
        String content,
        MessageState state
) {
}
