package by.bsuir.publisherservice.client.discussion.response;

import by.bsuir.publisherservice.entity.MessageState;

public record DiscussionMessageResponseTo(
        Long id,
        Long storyId,
        String content,
        MessageState state
) {
}
