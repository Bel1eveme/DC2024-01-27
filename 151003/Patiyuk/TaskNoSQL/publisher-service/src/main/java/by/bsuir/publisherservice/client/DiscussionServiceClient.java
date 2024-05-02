package by.bsuir.publisherservice.client;

import by.bsuir.publisherservice.client.request.DiscussionMessageRequestTo;
import by.bsuir.publisherservice.client.response.DiscussionMessageResponseTo;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.*;

import java.util.List;

@HttpExchange("/messages")
public interface DiscussionServiceClient {
    @GetExchange
    List<DiscussionMessageResponseTo> getAllMessages(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size);

    @GetExchange("/story/{id}")
    List<DiscussionMessageResponseTo> getMessagesByStoryId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size);

    @GetExchange("/{id}")
    DiscussionMessageResponseTo getMessageById(@PathVariable Long id);

    @PostExchange
    DiscussionMessageResponseTo createMessage(@Valid @RequestBody DiscussionMessageRequestTo message);

    @PutExchange
    DiscussionMessageResponseTo updateMessage(@Valid @RequestBody DiscussionMessageRequestTo message);

    @DeleteExchange("/{id}")
    void deleteMessage(@PathVariable Long id);
}
