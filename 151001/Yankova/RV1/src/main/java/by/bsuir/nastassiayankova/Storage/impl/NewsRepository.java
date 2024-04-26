package by.bsuir.nastassiayankova.Storage.impl;

import by.bsuir.nastassiayankova.Entity.News;
import by.bsuir.nastassiayankova.Storage.InMemoryRepository;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NewsRepository implements InMemoryRepository<News> {
    private final Map<Long, News> NewsMemory = new HashMap<>();

    @Override
    public News get(long id) {
        News news = NewsMemory.get(id);
        if (news != null) {
            news.setId(id);
        }
        return news;
    }

    @Override
    public List<News> getAll() {
        List<News> newsList = new ArrayList<>();
        for (Long key : NewsMemory.keySet()) {
            News news = NewsMemory.get(key);
            news.setId(key);
            newsList.add(news);
        }
        return newsList;
    }

    @Override
    public News delete(long id) {
        return NewsMemory.remove(id);
    }

    @Override
    public News insert(News insertObject) {
        NewsMemory.put(insertObject.getId(), insertObject);
        return insertObject;
    }

    @Override
    public boolean update(News updatingValue) {
        return NewsMemory.replace(updatingValue.getId(), NewsMemory.get(updatingValue.getId()), updatingValue);
    }
}
