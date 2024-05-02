package by.rusakovich.publisher.generics.spi.dao;

import by.rusakovich.publisher.generics.model.IEntity;

import java.util.Optional;

public interface IEntityRepository <Id, Entity extends IEntity<Id>>{
    Optional<Entity> readById(Id id);
    void removeById(Id id);
    Optional<Entity> create(Entity entity);
    Optional<Entity> update(Entity entity);
    Iterable<Entity> readAll();
}
