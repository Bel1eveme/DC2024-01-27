package com.example.publicator.dao;

import com.example.publicator.model.Creator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

//@Repository
public class CreatorDaoImpl implements CreatorDao {
    private static final Map<Integer, Creator> map = new HashMap();
    private static final AtomicInteger idHolder = new AtomicInteger();


    @Override
    public Creator create(Creator entity) {
        int creatorId = idHolder.incrementAndGet();
        entity.setId(creatorId);
        map.put(creatorId, entity);
        return entity;
    }

    @Override
    public List<Creator> readAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public Creator read(int id) {
        return map.getOrDefault(id, null);
    }

    @Override
    public Creator update(Creator entity, int id) {
        if (map.containsKey(id)) {
            entity.setId(id);
            map.put(id, entity);
            return entity;
        } else
            return null;
    }

    @Override
    public boolean delete(int id) {
        if (map.containsKey(id)) {
            map.remove(id);
            return true;
        } else
            return false;
    }
}
