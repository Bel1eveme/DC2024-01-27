package org.example.publisher.api.kafka.producer;

import org.example.publisher.api.kafka.consumer.NoteConsumer;
import org.example.publisher.impl.note.dto.NoteResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class NoteProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "OutTopic";
    private final NoteConsumer noteConsumer;

    @Autowired
    public NoteProducer(KafkaTemplate<String, String> kafkaTemplate, NoteConsumer noteConsumer) {
        this.kafkaTemplate = kafkaTemplate;
        this.noteConsumer = noteConsumer;
    }

    public NoteResponseTo sendNote(String method, String message, boolean isShouldWait) throws InterruptedException {
        UUID requestId = UUID.randomUUID();
        kafkaTemplate.send(TOPIC, "requestId=" + requestId + "," + method + ":" + message);
        if (isShouldWait) {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            noteConsumer.subscribeToResponse(countDownLatch, requestId.toString());
            if (countDownLatch.await(10, TimeUnit.SECONDS)) {
                return noteConsumer.getResponse();
            } else {
                throw new RuntimeException("Timeout exceed");
            }
        } else {
            return null;
        }
    }
}

