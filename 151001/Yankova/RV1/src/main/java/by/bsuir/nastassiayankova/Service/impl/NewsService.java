package by.bsuir.nastassiayankova.Service.impl;

import by.bsuir.nastassiayankova.Entity.News;
import by.bsuir.nastassiayankova.Service.IService;
import by.bsuir.nastassiayankova.Dto.NewsMapper;
import by.bsuir.nastassiayankova.Dto.impl.NewsRequestTo;
import by.bsuir.nastassiayankova.Dto.impl.NewsResponseTo;
import by.bsuir.nastassiayankova.Storage.impl.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NewsService implements IService<NewsResponseTo, NewsRequestTo> {
    @Autowired
    private NewsRepository newsRepository;

    public List<NewsResponseTo> getAll() {
        List<News> newsList = newsRepository.getAll();
        List<NewsResponseTo> resultList = new ArrayList<>();
        for (News news : newsList) {
            resultList.add(NewsMapper.INSTANCE.NewsToNewsResponseTo(news));
        }
        return resultList;
    }

    public NewsResponseTo update(NewsRequestTo updatingNews) {
        News news = NewsMapper.INSTANCE.NewsRequestToToNews(updatingNews);
        if (validateNews(news)) {
            boolean result = newsRepository.update(news);
            return result ? NewsMapper.INSTANCE.NewsToNewsResponseTo(news) : null;
        } else return new NewsResponseTo();
        //return responseTo;
    }

    public NewsResponseTo get(long id) {
        return NewsMapper.INSTANCE.NewsToNewsResponseTo(newsRepository.get(id));
    }

    public NewsResponseTo delete(long id) {
        return NewsMapper.INSTANCE.NewsToNewsResponseTo(newsRepository.delete(id));
    }

    public NewsResponseTo add(NewsRequestTo newsRequestTo) {
        News news = NewsMapper.INSTANCE.NewsRequestToToNews(newsRequestTo);
        return NewsMapper.INSTANCE.NewsToNewsResponseTo(newsRepository.insert(news));
    }

    private boolean validateNews(News news) {
        String title = news.getTitle();
        String content = news.getContent();

        return (content.length() >= 4 && content.length() <= 2048) && (title.length() >= 2 && title.length() <= 64);
    }
}
