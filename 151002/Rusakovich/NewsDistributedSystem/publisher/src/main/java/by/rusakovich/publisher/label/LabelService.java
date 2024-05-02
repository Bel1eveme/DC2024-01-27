package by.rusakovich.publisher.label;

import by.rusakovich.publisher.generics.spi.dao.IEntityRepository;
import by.rusakovich.publisher.generics.EntityService;
import by.rusakovich.publisher.generics.spi.redis.IRedisClient;
import by.rusakovich.publisher.generics.model.IEntityMapper;
import by.rusakovich.publisher.label.model.Label;
import by.rusakovich.publisher.label.model.LabelRequestTO;
import by.rusakovich.publisher.label.model.LabelResponseTO;
import org.springframework.stereotype.Service;

@Service
public class LabelService extends EntityService<Long, LabelRequestTO, LabelResponseTO, Label> {
    public LabelService(
            IEntityMapper<Long, Label, LabelRequestTO, LabelResponseTO> mapper,
            IEntityRepository<Long, Label> rep,
            IRedisClient<LabelResponseTO, Long> redisClient) {
        super(mapper, rep, redisClient);
    }
}
