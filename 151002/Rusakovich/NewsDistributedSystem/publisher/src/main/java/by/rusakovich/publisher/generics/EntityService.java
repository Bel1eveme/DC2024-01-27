package by.rusakovich.publisher.generics;

import by.rusakovich.publisher.error.exception.CantCreate;
import by.rusakovich.publisher.error.exception.NotFound;
import by.rusakovich.publisher.generics.model.IEntity;
import by.rusakovich.publisher.generics.model.IEntityMapper;
import by.rusakovich.publisher.generics.spi.dao.IEntityRepository;
import by.rusakovich.publisher.generics.spi.redis.IRedisClient;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
public abstract class EntityService<Id, RequestTO, ResponseTO, Entity extends IEntity<Id>> implements IEntityService<Id, RequestTO, ResponseTO> {

    private IEntityMapper<Id, Entity, RequestTO, ResponseTO> mapper;
    private IEntityRepository<Id, Entity> rep;
    private IRedisClient<ResponseTO, Id> redisClient;

    @Override
    public ResponseTO readById(Id id) {
        var response = redisClient.get(id);
        if(response == null) {
            var entity = rep.readById(id).orElseThrow(() -> new NotFound(id));
            response = mapper.mapToResponse(entity);
            redisClient.put(id, response);
        }
        return response;
    }

    @Override
    public List<ResponseTO> readAll() {
        return StreamSupport.stream(rep.readAll().spliterator(), false)
                .map(mapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseTO create(RequestTO newEntity) {
        var mappedNewEntity= mapper.mapToEntity(newEntity);
        var entity = rep.create(mappedNewEntity).orElseThrow(CantCreate::new);
        var response = mapper.mapToResponse(entity);
        redisClient.put(entity.getId(), response); // to don't implement anything
        return response;
    }

    @Override
    public ResponseTO update(RequestTO updatedEntity) {
        var mappedEntityToUpdate = mapper.mapToEntity(updatedEntity);
        var entity = rep.update(mappedEntityToUpdate).orElseThrow(() -> new NotFound(mappedEntityToUpdate.getId()));
        var response = mapper.mapToResponse(entity);
        redisClient.put(entity.getId(), response);
        return response;
    }

    @Override
    public void deleteById(Id id) {
        rep.removeById(id);
        redisClient.delete(id);
    }
}
