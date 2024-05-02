package org.example.discussion.api.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.discussion.impl.note.dto.NoteResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NoteProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "InTopic";

    @Autowired
    public NoteProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendResponseNote(NoteResponseTo note, String requestId) {
        ObjectMapper mapper = new ObjectMapper();
        String message = "";
        try {
            message = mapper.writeValueAsString(note);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        kafkaTemplate.send(TOPIC, requestId + "," + message);

    }
}
