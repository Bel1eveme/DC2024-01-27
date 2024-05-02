package org.example.publisher.api.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.example.publisher.impl.note.dto.NoteResponseTo;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class NoteConsumer {
    private static final String TOPIC_NAME = "InTopic";

    private CountDownLatch latch;

    @Getter
    private NoteResponseTo response;
    private String requestId;

    @KafkaListener(topics = TOPIC_NAME, groupId = "group")
    public void listen(String message) throws JsonProcessingException {
        if (message.startsWith("requestId="+requestId)) {
            ObjectMapper mapper = new ObjectMapper();
            this.response = mapper.readValue(message.substring(message.indexOf("{")), NoteResponseTo.class);
            latch.countDown();
        }
    }

    public void subscribeToResponse(CountDownLatch latch, String requestId) {
        this.latch = latch;
        this.requestId = requestId;
    }

}
