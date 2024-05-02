package com.example.discussion.controllers;


import com.example.discussion.dto.MessageResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, MessageResponseTo> kafkaTemplate;

    void sendMessage(MessageResponseTo messageResponseTo, String topicName) {
        kafkaTemplate.send(topicName, messageResponseTo);
    }
}
