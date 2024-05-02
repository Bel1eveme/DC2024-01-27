package by.bsuir.publisherservice.client.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DiscussionMessageRequestTo(
        Long id,
        Long storyId,
        String country,
        @NotNull @Size(min = 2, max = 2048)
        String content
) {
}
