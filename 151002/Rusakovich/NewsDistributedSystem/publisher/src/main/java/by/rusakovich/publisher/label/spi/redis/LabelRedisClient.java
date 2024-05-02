package by.rusakovich.publisher.label.spi.redis;

import by.rusakovich.publisher.generics.spi.redis.RedisClient;
import by.rusakovich.publisher.label.model.LabelResponseTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class LabelRedisClient extends RedisClient<String, Long, LabelResponseTO> {

    protected LabelRedisClient(RedisTemplate<String, LabelResponseTO> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected String getKey() {
        return "publisher:label:response";
    }
}
