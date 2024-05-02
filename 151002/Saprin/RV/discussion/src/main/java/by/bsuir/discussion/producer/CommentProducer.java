package by.bsuir.discussion.producer;

import by.bsuir.discussion.bean.Comment;
import by.bsuir.discussion.dto.CommentResponseTo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CommentProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;


    private static final String TOPIC = "InTopic";

    @Autowired
    public CommentProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(CommentResponseTo comment) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(comment) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        kafkaTemplate.send(TOPIC, json);
    }

}