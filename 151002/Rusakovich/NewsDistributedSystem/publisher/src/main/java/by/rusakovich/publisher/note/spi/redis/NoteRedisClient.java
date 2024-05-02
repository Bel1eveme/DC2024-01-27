package by.rusakovich.publisher.note.spi.redis;

import by.rusakovich.publisher.generics.spi.redis.RedisClient;
import by.rusakovich.publisher.note.model.NoteResponseTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class NoteRedisClient extends RedisClient<String, Long, NoteResponseTO> {

    protected NoteRedisClient(RedisTemplate<String, NoteResponseTO> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected String getKey() {
        return "publisher:note:response";
    }
}
