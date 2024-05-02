package by.bsuir.discussionservice.dto.message;

import by.bsuir.discussionservice.dto.request.MessageRequestTo;
import lombok.Builder;

@Builder
public record InTopicMessage(
        Operation operation,
        MessageRequestTo message,
        Integer page,
        Integer size
) {
}
