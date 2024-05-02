package by.bsuir.publisherservice.client.discussion;

import by.bsuir.publisherservice.client.discussion.request.DiscussionMessageRequestTo;
import by.bsuir.publisherservice.client.discussion.response.DiscussionMessageResponseTo;

import java.util.List;

public interface DiscussionServiceClient {
    List<DiscussionMessageResponseTo> getAllMessages(Integer page, Integer size);

    List<DiscussionMessageResponseTo> getMessagesByStoryId(Long id, Integer page, Integer size);

    DiscussionMessageResponseTo getMessageById(Long id);

    DiscussionMessageResponseTo createMessage(DiscussionMessageRequestTo message);

    DiscussionMessageResponseTo updateMessage(DiscussionMessageRequestTo message);

    void deleteMessage(Long id);
}
