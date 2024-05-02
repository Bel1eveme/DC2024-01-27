package by.bsuir.publisherservice.dto.response;

import by.bsuir.publisherservice.entity.MessageState;

public record MessageResponseTo(
        Long id,
        Long storyId,
        String content,
        MessageState state
) {
}
