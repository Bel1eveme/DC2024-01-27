package by.bsuir.controllers;

import by.bsuir.dto.PostRequestTo;
import by.bsuir.dto.PostResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSender {
    @Autowired
    private KafkaTemplate<String, PostRequestTo> userKafkaTemplate;


    void sendCustomMessage(PostRequestTo postRequestTo, String topicName) {
        userKafkaTemplate.send(topicName, postRequestTo);
    }
}