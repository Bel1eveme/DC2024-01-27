package by.bsuir.publisherservice.client.discussion.request;

import by.bsuir.publisherservice.entity.MessageState;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record DiscussionMessageRequestTo(
        Long id,
        Long storyId,
        String country,
        @NotNull @Size(min = 2, max = 2048)
        String content,
        MessageState state
) {
}
