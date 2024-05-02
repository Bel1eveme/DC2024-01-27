package by.bsuir.publisherservice.dto.message;

import by.bsuir.publisherservice.client.discussion.request.DiscussionMessageRequestTo;
import lombok.Builder;

@Builder
public record InTopicMessage(
        Operation operation,
        DiscussionMessageRequestTo message,
        Integer page,
        Integer size
) {
}
