package by.bsuir.discussionservice.service.kafka;

import by.bsuir.discussionservice.dto.message.InTopicMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {
    private final MessageHandler messageHandler;

    @KafkaListener(topics = "${spring.kafka.topic.request}", groupId = "discussion-group")
    public void listen(ConsumerRecord<String, InTopicMessage> message) {
        log.info("Received message: {}", message.value());
        messageHandler.processMessage(message.value(), message.key());
    }
}
