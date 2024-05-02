package com.poluectov.rvproject.service.redis;

import com.poluectov.rvproject.dto.message.MessageResponseTo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class RedisClientCacheService implements RedisCacheService<Long, MessageResponseTo> {

    private static final String REDIS_PUBLISHER_KEY = "publisher:message:response";
    RedisTemplate<String, MessageResponseTo> redisTemplate;

    @Override
    public MessageResponseTo get(Long key) {
        log.info("Get from redis: {}", key);
        return (MessageResponseTo) redisTemplate.opsForHash().get(REDIS_PUBLISHER_KEY, key);
    }

    @Override
    public void put(Long key, MessageResponseTo value) {
        log.info("Put to redis key: {}, value: {}", key, value);
        redisTemplate.opsForHash().put(REDIS_PUBLISHER_KEY, key, value);
    }

    @Override
    public void delete(Long key) {
        log.info("Delete from redis: {}", key);
        redisTemplate.opsForHash().delete(REDIS_PUBLISHER_KEY, key);
    }
}
