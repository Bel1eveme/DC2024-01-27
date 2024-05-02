package by.rusakovich.publisher.news;

import by.rusakovich.publisher.generics.spi.dao.IEntityRepository;
import by.rusakovich.publisher.generics.EntityService;
import by.rusakovich.publisher.generics.spi.redis.IRedisClient;
import by.rusakovich.publisher.generics.model.IEntityMapper;
import by.rusakovich.publisher.news.model.News;
import by.rusakovich.publisher.news.model.NewsRequestTO;
import by.rusakovich.publisher.news.model.NewsResponseTO;
import org.springframework.stereotype.Service;

@Service
public class NewsService extends EntityService<Long, NewsRequestTO, NewsResponseTO, News> {

    public NewsService(
            IEntityMapper<Long, News, NewsRequestTO, NewsResponseTO> mapper,
            IEntityRepository<Long, News> rep,
            IRedisClient<NewsResponseTO, Long> redisClient) {
        super(mapper, rep, redisClient);
    }
}
