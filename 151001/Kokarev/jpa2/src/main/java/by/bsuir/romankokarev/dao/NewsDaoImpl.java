package by.bsuir.romankokarev.dao;

import by.bsuir.romankokarev.model.News;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class NewsDaoImpl implements NewsDao {
    Map<Integer, News> map = new HashMap<>();
    private static final AtomicInteger idHolder = new AtomicInteger();

    @Override
    public News create(News entity) {
        int id = idHolder.incrementAndGet();
        entity.setId(id);
        map.put(id, entity);

        return entity;
    }

    @Override
    public News read(int id) {
        return map.getOrDefault(id, null);
    }

    @Override
    public List<News> readAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public News update(News entity, int id) {
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
