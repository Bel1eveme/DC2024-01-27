package by.rusakovich.publisher.author.spi.dao;

import by.rusakovich.publisher.author.model.Author;
import by.rusakovich.publisher.generics.spi.dao.EntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAuthorRepository extends EntityRepository<Long, Author> {}
