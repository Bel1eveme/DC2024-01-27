package by.rusakovich.publisher.news.spi.dao;

import by.rusakovich.publisher.generics.spi.dao.EntityRepository;
import by.rusakovich.publisher.news.model.News;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaNewsRepository extends EntityRepository<Long, News> {}
