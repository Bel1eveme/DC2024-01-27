package by.bsuir.romankokarev.dao;

import by.bsuir.romankokarev.model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageDaoImpl implements MessageDao {

    Map<Integer, Message> map = new HashMap<>();
    private static final AtomicInteger idHolder = new AtomicInteger();

    @Override
    public Message create(Message entity) {
        int id = idHolder.incrementAndGet();
        entity.setId(id);
        map.put(id, entity);

        return entity;
    }

    @Override
    public Message read(int id) {
        return map.getOrDefault(id, null);
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(map.values());
    }


    @Override
    public Message update(Message entity, int id) {
        if (map.containsKey(id)) {
            entity.setId(id);
            map.put(id, entity);

            return entity;
        }

        return null;
    }

    @Override
    public boolean delete(int id) {
        if (map.containsKey(id)) {
            map.remove(id);

            return true;
        }

        return false;
    }
}
