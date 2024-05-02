package by.bsuir.romankokarev.dao;

import by.bsuir.romankokarev.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UserDaoImpl implements UserDao {
    private static final Map<Integer, User> map = new HashMap();
    private static final AtomicInteger idHolder = new AtomicInteger();


    @Override
    public User create(User entity) {
        int userId = idHolder.incrementAndGet();
        entity.setId(userId);
        map.put(userId, entity);

        return entity;
    }

    @Override
    public User read(int id) {
        return map.getOrDefault(id, null);
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public User update(User entity, int id) {
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
