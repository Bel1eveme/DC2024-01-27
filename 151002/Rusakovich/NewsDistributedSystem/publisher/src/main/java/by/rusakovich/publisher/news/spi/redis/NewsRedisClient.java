package by.rusakovich.publisher.news.spi.redis;

import by.rusakovich.publisher.generics.spi.redis.RedisClient;
import by.rusakovich.publisher.news.model.NewsResponseTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class NewsRedisClient extends RedisClient<String, Long, NewsResponseTO> {
    protected NewsRedisClient(RedisTemplate<String, NewsResponseTO> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected String getKey() {
        return "publisher:news:response";
    }
}
