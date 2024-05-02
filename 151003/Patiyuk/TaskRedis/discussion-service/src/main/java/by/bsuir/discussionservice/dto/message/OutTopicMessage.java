package by.bsuir.discussionservice.dto.message;

import by.bsuir.discussionservice.dto.response.MessageResponseTo;
import lombok.Builder;

import java.util.List;

@Builder
public record OutTopicMessage(
        Operation operation,
        String error,
        List<MessageResponseTo> result
) {
    public boolean isSuccessful() {
        return error == null;
    }
}
