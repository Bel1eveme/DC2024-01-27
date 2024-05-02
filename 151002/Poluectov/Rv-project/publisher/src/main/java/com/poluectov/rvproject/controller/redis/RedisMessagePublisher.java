package com.poluectov.rvproject.controller.redis;

import com.poluectov.rvproject.dto.KafkaMessageResponseTo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class RedisMessagePublisher implements MessagePublisher<KafkaMessageResponseTo> {

    RedisTemplate<String, KafkaMessageResponseTo> redisTemplate;

    private ChannelTopic topic;

    public void publish(KafkaMessageResponseTo message){
        log.info("REDIS: Message sent: {}", message);
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
