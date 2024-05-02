package by.rusakovich.publisher.generics.spi.dao;

import by.rusakovich.publisher.error.exception.NotFound;
import by.rusakovich.publisher.generics.model.IEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EntityRepository<Id, Entity extends IEntity<Id>> extends CrudRepository<Entity, Id>, IEntityRepository<Id, Entity>{

    @Override
    default Optional<Entity> readById(Id id){
        return findById(id);
    }

    @Override
    default Optional<Entity> create(Entity entity){
        return Optional.of(save(entity));
    }

    @Override
    default Optional<Entity> update(Entity entity){
        if(existsById(entity.getId())){
            save(entity);
            return Optional.of(entity);
        } else {
            return Optional.empty();
        }
    }

    @Override
    default Iterable<Entity> readAll(){
        return findAll();
    }

    @Override
    default void removeById(Id id){
        if(existsById(id)){
            deleteById(id);
        }else{
            throw new NotFound(id);
        }
    }
}
