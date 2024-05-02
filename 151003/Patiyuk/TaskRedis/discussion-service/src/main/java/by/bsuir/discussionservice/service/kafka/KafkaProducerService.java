package by.bsuir.discussionservice.service.kafka;

import by.bsuir.discussionservice.dto.message.OutTopicMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, OutTopicMessage> kafkaTemplate;

    public void sendMessage(OutTopicMessage message, String key, String topic) {
        log.info("Sending message: {}", message);
        kafkaTemplate.send(topic, key, message);
    }
}
