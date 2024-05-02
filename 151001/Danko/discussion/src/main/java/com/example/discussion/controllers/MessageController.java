package com.example.discussion.controllers;

import com.example.discussion.dto.MessageRequestTo;
import com.example.discussion.dto.MessageResponseTo;
import com.example.discussion.services.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1.0/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private KafkaSender kafkaSender;

    @KafkaListener(topics = "InTopic", groupId = "inGroup",
            containerFactory = "messageRequestToConcurrentKafkaListenerContainerFactory")
    void listenerWithMessageConverter(@Payload MessageRequestTo messageRequestTo) {
        String kafkaTopicName_OutTopic = "OutTopic";
        if (Objects.equals(messageRequestTo.getMethod(), "GET")) {
            if (messageRequestTo.getId() != null) {
                kafkaSender.sendMessage(getById(messageRequestTo.getId()), kafkaTopicName_OutTopic);
            }
        } else {
            if (Objects.equals(messageRequestTo.getMethod(), "DELETE")) {
                kafkaSender.sendMessage(delete(messageRequestTo.getId()), kafkaTopicName_OutTopic);
            } else {
                if (Objects.equals(messageRequestTo.getMethod(), "POST")) {
                    kafkaSender.sendMessage(save(messageRequestTo.getCountry(), messageRequestTo), kafkaTopicName_OutTopic);
                } else {
                    if (Objects.equals(messageRequestTo.getMethod(), "PUT")) {
                        kafkaSender.sendMessage(update(messageRequestTo.getCountry(), messageRequestTo), kafkaTopicName_OutTopic);
                    }
                }
            }
        }
    }

    @GetMapping
    public List<MessageResponseTo> getAll() {
        return messageService.getAll();
    }
    @GetMapping("/{id}")
    public MessageResponseTo getById(@PathVariable Long id) {
        return messageService.getById(id);
    }

    @DeleteMapping("/{id}")
    public MessageResponseTo delete(@PathVariable Long id) {
        messageService.delete(id);
        return new MessageResponseTo(null, null, null);
    }

    @PostMapping
    public MessageResponseTo save(@RequestHeader("Accept-Language") String lang, @RequestBody MessageRequestTo message) {
        return messageService.save(message, lang);
    }

    @PutMapping()
    public MessageResponseTo update(@RequestHeader("Accept-Language") String lang, @RequestBody MessageRequestTo message) {
        return messageService.update(message, lang);
    }

    @GetMapping("/story/{id}")
    public ResponseEntity<List<MessageResponseTo>> getByStoryId(@PathVariable Long id){
        return ResponseEntity.status(200).body(messageService.getByStoryId(id));
    }
}
