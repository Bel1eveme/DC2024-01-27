package dtalalaev.rv.impl.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtalalaev.rv.impl.model.post.PostResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PostProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;


    private static final String TOPIC = "InTopic";

    @Autowired
    public PostProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(PostResponseTo post) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(post) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        kafkaTemplate.send(TOPIC, json);
    }

    public void sendMessage(String post) {
        kafkaTemplate.send(TOPIC, post);
    }
}