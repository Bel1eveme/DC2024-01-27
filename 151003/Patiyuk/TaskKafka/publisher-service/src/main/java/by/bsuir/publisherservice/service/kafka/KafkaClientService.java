package by.bsuir.publisherservice.service.kafka;

import by.bsuir.publisherservice.dto.message.InTopicMessage;
import by.bsuir.publisherservice.dto.message.OutTopicMessage;
import by.bsuir.publisherservice.exception.ExchangeFailedException;
import by.bsuir.publisherservice.exception.ExchangeTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaClientService {
    private final KafkaTemplate<String, InTopicMessage> kafkaTemplate;
    private final ConcurrentHashMap<String, CompletableFuture<OutTopicMessage>> cache = new ConcurrentHashMap<>();

    public void sendMessage(InTopicMessage message, String topic) {
        kafkaTemplate.send(topic, createId(), message);
    }

    public OutTopicMessage sendSyncMessage(InTopicMessage message, String topic) {
        String id = createId();
        CompletableFuture<OutTopicMessage> future = new CompletableFuture<>();
        cache.put(id, future);
        log.info("Sending message: {}", message);
        try {
            kafkaTemplate.send(topic, id, message);
            return future.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            cache.remove(id);
            log.error("Timeout while waiting for response");
            throw new ExchangeTimeoutException("Timeout while waiting for response");
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Failed to exchange messages", e);
            throw new ExchangeFailedException("Failed to exchange messages");
        }
    }

    @KafkaListener(topics = "${spring.kafka.topic.response}", groupId = "group_id-#{T(java.util.UUID).randomUUID().toString()}")
    private void listen(ConsumerRecord<String, OutTopicMessage> response) {
        String key = response.key();
        OutTopicMessage message = response.value();
        log.info("Received message: {}", message);
        CompletableFuture<OutTopicMessage> future = cache.remove(key);
        if (future != null) {
            log.info("Completing future: {}", message);
            future.complete(message);
        }
    }

    private static String createId() {
        return UUID.randomUUID().toString();
    }
}
