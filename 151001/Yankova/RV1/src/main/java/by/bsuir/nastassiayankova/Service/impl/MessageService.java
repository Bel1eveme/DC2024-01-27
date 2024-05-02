package by.bsuir.nastassiayankova.Service.impl;

import by.bsuir.nastassiayankova.Entity.Message;
import by.bsuir.nastassiayankova.Service.IService;
import by.bsuir.nastassiayankova.Dto.*;
import by.bsuir.nastassiayankova.Dto.impl.MessageRequestTo;
import by.bsuir.nastassiayankova.Dto.impl.MessageResponseTo;
import by.bsuir.nastassiayankova.Storage.impl.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageService implements IService<MessageResponseTo, MessageRequestTo> {
    @Autowired
    private MessageRepository messageRepository;

    public List<MessageResponseTo> getAll() {
        List<Message> messageList = messageRepository.getAll();
        List<MessageResponseTo> resultList = new ArrayList<>();
        for (Message message : messageList) {
            resultList.add(MessageMapper.INSTANCE.MessageToMessageResponseTo(message));
        }
        return resultList;
    }

    public MessageResponseTo update(MessageRequestTo updatingMessage) {
        Message message = MessageMapper.INSTANCE.MessageRequestToToMessage(updatingMessage);
        if (validateMessage(message)) {
            boolean result = messageRepository.update(message);
            return result ? MessageMapper.INSTANCE.MessageToMessageResponseTo(message) : null;
        } else return new MessageResponseTo();
    }

    public MessageResponseTo get(long id) {
        return MessageMapper.INSTANCE.MessageToMessageResponseTo(messageRepository.get(id));
    }

    public MessageResponseTo delete(long id) {
        return MessageMapper.INSTANCE.MessageToMessageResponseTo(messageRepository.delete(id));
    }

    public MessageResponseTo add(MessageRequestTo messageRequestTo) {
        Message message = MessageMapper.INSTANCE.MessageRequestToToMessage(messageRequestTo);
        return MessageMapper.INSTANCE.MessageToMessageResponseTo(messageRepository.insert(message));
    }

    private boolean validateMessage(Message message) {
        String name = message.getContent();
        return name.length() >= 2 && name.length() <= 32;
    }
}
