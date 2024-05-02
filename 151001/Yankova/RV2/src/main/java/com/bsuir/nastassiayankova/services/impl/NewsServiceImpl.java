package com.bsuir.nastassiayankova.services.impl;

import com.bsuir.nastassiayankova.beans.entity.News;
import com.bsuir.nastassiayankova.beans.entity.User;
import com.bsuir.nastassiayankova.beans.entity.ValidationMarker;
import com.bsuir.nastassiayankova.mappers.NewsListMapper;
import com.bsuir.nastassiayankova.mappers.NewsMapper;
import com.bsuir.nastassiayankova.beans.request.NewsRequestTo;
import com.bsuir.nastassiayankova.beans.response.NewsResponseTo;
import com.bsuir.nastassiayankova.exceptions.NoSuchUserException;
import com.bsuir.nastassiayankova.exceptions.NoSuchNewsException;
import com.bsuir.nastassiayankova.services.NewsService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.bsuir.nastassiayankova.repositories.NewsRepository;
import com.bsuir.nastassiayankova.services.UserService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Validated
public class NewsServiceImpl implements NewsService {
    private final UserService userService;
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final NewsListMapper newsListMapper;

    @Autowired
    public NewsServiceImpl(UserService userService, NewsRepository newsRepository, NewsMapper newsMapper, NewsListMapper newsListMapper) {
        this.userService = userService;
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
        this.newsListMapper = newsListMapper;
    }

    @Override
    @Validated(ValidationMarker.OnCreate.class)
    public NewsResponseTo create(@Valid NewsRequestTo entity) {
        User user = userService.findUserByIdExt(entity.userId()).orElseThrow(() -> new NoSuchUserException(entity.userId()));
        News news = newsMapper.toNews(entity);
        news.setUser(user);
        return newsMapper.toNewsResponseTo(newsRepository.save(news));
    }

    @Override
    public List<NewsResponseTo> read() {
        return newsListMapper.toNewsResponseToList(newsRepository.findAll());
    }

    @Override
    @Validated(ValidationMarker.OnUpdate.class)
    public NewsResponseTo update(@Valid NewsRequestTo entity) {
        if (newsRepository.existsById(entity.id())) {
            News news = newsMapper.toNews(entity);
            News newsRef = newsRepository.getReferenceById(news.getId());
            news.setUser(newsRef.getUser());
            news.setMessageList(newsRef.getMessageList());
            news.setNewsTagList(newsRef.getNewsTagList());
            //  tweetResponseTo.stickerList() = news.getNewsTagList().stream().map(element -> stickerMapper.toTagResponseTo(element.getTag())).collect(Collectors.toList());
            return newsMapper.toNewsResponseTo(newsRepository.save(news));
        } else {
            throw new NoSuchUserException(entity.id());
        }
    }

    @Override
    public void delete(Long id) {
        if (newsRepository.existsById(id)) {
            newsRepository.deleteById(id);
        } else {
            throw new NoSuchNewsException(id);
        }

    }

    @Override
    public NewsResponseTo findNewsById(Long id) {
        News news = newsRepository.findById(id).orElseThrow(() -> new NoSuchNewsException(id));
        return newsMapper.toNewsResponseTo(news);
    }

    @Override
    public Optional<News> findNewsByIdExt(Long id) {
        return newsRepository.findById(id);
    }
}
