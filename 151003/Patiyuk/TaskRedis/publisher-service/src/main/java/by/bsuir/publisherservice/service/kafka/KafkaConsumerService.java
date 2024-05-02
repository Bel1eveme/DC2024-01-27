package by.bsuir.publisherservice.service.kafka;

import by.bsuir.publisherservice.client.discussion.mapper.DiscussionMessageMapper;
import by.bsuir.publisherservice.dto.message.OutTopicMessage;
import by.bsuir.publisherservice.dto.response.MessageResponseTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {
    private final RedisTemplate<String, MessageResponseTo> redis;
    private final DiscussionMessageMapper mapper;
    private static final String PREFIX = "messages::";

    @KafkaListener(topics = "${spring.kafka.topic.create}", groupId = "publisher-service")
    public void listenCreate(OutTopicMessage message) {
        log.info("Received message: {}", message);
        Optional.of(message)
                .filter(OutTopicMessage::isSuccessful)
                .map(OutTopicMessage::result)
                .map(List::getFirst)
                .map(mapper::toMessageResponse)
                .ifPresent(this::cache);
    }

    private void cache(MessageResponseTo response) {
        log.info("Caching message: {}", response);
        redis.delete(PREFIX + "*");
        redis.opsForValue().set(PREFIX + response.id(), response);
    }
}
