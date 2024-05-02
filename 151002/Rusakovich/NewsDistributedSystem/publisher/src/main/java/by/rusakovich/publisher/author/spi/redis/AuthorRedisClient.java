package by.rusakovich.publisher.author.spi.redis;

import by.rusakovich.publisher.author.model.AuthorResponseTO;
import by.rusakovich.publisher.generics.spi.redis.RedisClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuthorRedisClient extends RedisClient<String, Long, AuthorResponseTO> {
    protected AuthorRedisClient(RedisTemplate<String, AuthorResponseTO> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected String getKey() {
        return "publisher:author:response";
    }
}
