package by.bsuir.publisherservice.dto.message;

import by.bsuir.publisherservice.client.discussion.response.DiscussionMessageResponseTo;

import java.util.List;

public record OutTopicMessage(
        Operation operation,
        String error,
        List<DiscussionMessageResponseTo> result
) {
    public boolean isSuccessful() {
        return error == null;
    }
}
