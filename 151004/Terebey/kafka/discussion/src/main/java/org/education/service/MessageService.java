package org.education.service;

import org.education.bean.Message;
import org.education.exception.NoSuchMessage;
import org.education.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final String country = "Belarus";

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAll(){
        return StreamSupport.stream(messageRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Message getById(int id){
        return messageRepository.findMessageById(id).orElseThrow(() -> new NoSuchMessage("No such comment with id"));
    }

    public Message create(Message message){
        message.setCountry("Belarus");
        return messageRepository.save(message);
    }

    public Message update(Message message){
        if(!messageRepository.existsById(message.getId())) throw new NoSuchMessage("There is no such comment with this id");
        message.setCountry(messageRepository.findMessageById(message.getId()).get().getCountry());
        return messageRepository.save(message);
    }

    public void delete(int id){
        if(!messageRepository.existsById(id)) throw new NoSuchMessage("There is no such comment with this id");
        messageRepository.delete(messageRepository.findMessageById(id).get());
    }

}