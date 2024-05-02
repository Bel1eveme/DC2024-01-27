package by.rusakovich.publisher.label.spi.dao;

import by.rusakovich.publisher.generics.spi.dao.EntityRepository;
import by.rusakovich.publisher.label.model.Label;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLabelRepository extends EntityRepository<Long, Label> {}
