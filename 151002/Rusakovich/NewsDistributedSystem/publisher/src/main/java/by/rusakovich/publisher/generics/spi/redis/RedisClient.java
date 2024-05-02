package by.rusakovich.publisher.generics.spi.redis;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public abstract class RedisClient<Key, HashKey, ResponseTO> implements IRedisClient<ResponseTO, HashKey> {

    private final RedisTemplate<Key, ResponseTO> redisTemplate;

    protected RedisClient(RedisTemplate<Key, ResponseTO> redisTemplate) {
        this.redisTemplate = redisTemplate;
        redisTemplate.expire(getKey(), 10, TimeUnit.SECONDS);
    }

    protected abstract Key getKey();

    @Override
    public void put(HashKey hashKey, ResponseTO responseTo) {
        redisTemplate.opsForHash().put(getKey(), hashKey, responseTo);
    }

    @Override
    public ResponseTO get(HashKey hashKey) {
        @SuppressWarnings("unchecked") ResponseTO responseTO = (ResponseTO) redisTemplate.opsForHash().get(getKey(), hashKey);
        return responseTO;
    }

    @Override
    public void delete(HashKey hashKey) {
        redisTemplate.opsForHash().delete(getKey(), hashKey);
    }
}
