package by.bsuir.discussionservice.dto.response;

import by.bsuir.discussionservice.entity.MessageState;

public record MessageResponseTo(
        Long id,
        Long storyId,
        String content,
        MessageState state
) {
}
