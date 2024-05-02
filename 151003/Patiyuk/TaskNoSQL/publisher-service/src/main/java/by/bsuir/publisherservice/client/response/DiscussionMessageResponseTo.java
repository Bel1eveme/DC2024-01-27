package by.bsuir.publisherservice.client.response;

public record DiscussionMessageResponseTo(
        Long id,
        Long storyId,
        String content
) {
}
