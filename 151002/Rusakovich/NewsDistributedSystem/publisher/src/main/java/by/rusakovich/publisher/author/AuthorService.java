package by.rusakovich.publisher.author;

import by.rusakovich.publisher.author.model.Author;
import by.rusakovich.publisher.author.model.AuthorRequestTO;
import by.rusakovich.publisher.author.model.AuthorResponseTO;
import by.rusakovich.publisher.generics.EntityService;
import by.rusakovich.publisher.generics.model.IEntityMapper;
import by.rusakovich.publisher.generics.spi.dao.IEntityRepository;
import by.rusakovich.publisher.generics.spi.redis.IRedisClient;
import org.springframework.stereotype.Service;

@Service
public class AuthorService extends EntityService<Long, AuthorRequestTO, AuthorResponseTO, Author> {
    public AuthorService(
        IEntityMapper<Long, Author, AuthorRequestTO, AuthorResponseTO> mapper,
        IEntityRepository<Long, Author> rep,
        IRedisClient<AuthorResponseTO, Long> redisClient) {
        super(mapper, rep, redisClient);
    }
}
