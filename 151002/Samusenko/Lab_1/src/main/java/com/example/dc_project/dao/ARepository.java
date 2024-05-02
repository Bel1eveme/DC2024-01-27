package com.example.dc_project.dao;

import com.example.dc_project.model.AEntity;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public abstract class ARepository<T extends AEntity> implements IRepository<Long, T> {
    private final AtomicLong ids = new AtomicLong();
    private final HashMap<Long, T> mapIdAndEntity = new HashMap<>();

    @Override
    public Optional<T> getById(Long id) {
        return Optional.ofNullable(mapIdAndEntity.get(id));
    }

    @Override
    public Iterable<T> getAll() {
        return mapIdAndEntity.values();
    }

    @Override
    public Optional<T> save(T entity) {
        if (entity == null) {
            throw new NullPointerException();
        }

        Long id = ids.incrementAndGet();
        entity.setId(id);

        mapIdAndEntity.put(id, entity);

        return Optional.of(entity);
    }

    @Override
    public Optional<T> update(T entity)
    {
        if (entity == null) {
            throw new NullPointerException();
        }

        Long entityId = entity.getId();

        if (entityId == null) {
            throw new NullPointerException();
        }

        mapIdAndEntity.put(entityId, entity);

        return Optional.of(entity);
    }

    @Override
    public boolean remove(T entity) {
        if (entity == null) {
            throw new NullPointerException();
        }

        Long entityId = entity.getId();

        if (entityId == null) {
            throw new NullPointerException();
        }

        return mapIdAndEntity.remove(entityId, entity);
    }

    @Override
    public boolean removeById(Long id) {
        if (id == null) {
            throw new NullPointerException();
        }

        return mapIdAndEntity.remove(id, mapIdAndEntity.get(id));
    }
}
