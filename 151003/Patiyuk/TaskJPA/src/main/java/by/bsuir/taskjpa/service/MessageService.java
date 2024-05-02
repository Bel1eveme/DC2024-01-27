package by.bsuir.taskrest.service;

import by.bsuir.taskrest.dto.request.MessageRequestTo;
import by.bsuir.taskrest.dto.response.MessageResponseTo;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface MessageService {
    List<MessageResponseTo> getAllMessages(PageRequest pageRequest);
    List<MessageResponseTo> getMessagesByStoryId(Long id, PageRequest pageRequest);
    MessageResponseTo getMessageById(Long id);
    MessageResponseTo createMessage(MessageRequestTo message);
    MessageResponseTo updateMessage(MessageRequestTo message);
    MessageResponseTo updateMessage(Long id, MessageRequestTo message);
    void deleteMessage(Long id);
}
