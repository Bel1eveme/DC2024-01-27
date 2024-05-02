package by.bsuir.publicator.producer;

import by.bsuir.publicator.consumer.CommentConsumer;
import by.bsuir.publicator.dto.CommentRequestTo;
import by.bsuir.publicator.dto.CommentResponseTo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import java.util.concurrent.CountDownLatch;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class CommentProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "OutTopic";
    private final CommentConsumer kafkaResponseListener;

    @Autowired
    public CommentProducer(KafkaTemplate<String, String> kafkaTemplate, CommentConsumer kafkaResponseListener) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaResponseListener = kafkaResponseListener;
    }

    public void sendMessage(CommentRequestTo comment) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(comment) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        kafkaTemplate.send(TOPIC, json);
    }

    public CommentResponseTo sendRequest(String methodType, String message) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        CommentResponseTo response = null;

        kafkaTemplate.send(TOPIC, methodType + ":" + message);

        kafkaResponseListener.setLatchAndResponse(latch, response);

        if (latch.await(10, TimeUnit.SECONDS)) {

            return kafkaResponseListener.getResponse();
        } else {
            throw new RuntimeException("Response timeout");
        }
    }
}
