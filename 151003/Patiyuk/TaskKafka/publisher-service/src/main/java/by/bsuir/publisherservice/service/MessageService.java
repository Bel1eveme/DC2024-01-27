package by.bsuir.publisherservice.service;

import by.bsuir.publisherservice.dto.request.MessageRequestTo;
import by.bsuir.publisherservice.dto.response.MessageResponseTo;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface MessageService {
    List<MessageResponseTo> getAllMessages(PageRequest pageRequest);
    List<MessageResponseTo> getMessagesByStoryId(Long id, PageRequest pageRequest);
    MessageResponseTo getMessageById(Long id);
    MessageResponseTo createMessage(MessageRequestTo message, String country);
    MessageResponseTo updateMessage(MessageRequestTo message, String country);
    MessageResponseTo updateMessage(Long id, MessageRequestTo message, String country);
    void deleteMessage(Long id);
}
