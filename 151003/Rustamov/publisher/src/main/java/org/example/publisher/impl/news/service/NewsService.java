package org.example.publisher.impl.news.service;

import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.impl.author.Author;
import org.example.publisher.impl.author.AuthorRepository;
import org.example.publisher.impl.news.News;
import org.example.publisher.impl.news.NewsRepository;
import org.example.publisher.impl.news.dto.NewsRequestTo;
import org.example.publisher.impl.news.dto.NewsResponseTo;
import org.example.publisher.impl.news.mapper.Impl.NewsMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNumeric;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final AuthorRepository authorRepository;
    private final NewsMapperImpl newsMapper;

    private final String ENTITY_NAME = "news";


    public List<NewsResponseTo> getNews() {
        List<News> news = newsRepository.findAll();
        List<NewsResponseTo> newsResponseTos = new ArrayList<>();

        for (var item : news) {
            newsResponseTos.add(newsMapper.newsToResponseTo(item));
        }
        return newsResponseTos;
    }


    public NewsResponseTo getNewsById(BigInteger id) throws EntityNotFoundException {
        Optional<News> news = newsRepository.findById(id);
        if (news.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        return newsMapper.newsToResponseTo(news.get());
    }

    public NewsResponseTo saveNews(NewsRequestTo newsRequestTo) throws EntityNotFoundException, DuplicateEntityException {
        Optional<Author> author = authorRepository.findById(newsRequestTo.getAuthorId());
        if (author.isEmpty()) {
            throw new EntityNotFoundException("Author", newsRequestTo.getAuthorId());
        }

        if (newsRequestTo.getCreated() == null) {
            newsRequestTo.setCreated(new Date());
        }
        if (newsRequestTo.getModified() == null) {
            newsRequestTo.setModified(new Date());
        }
        if (isNumeric(newsRequestTo.getContent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "iss_content should be a string, not a number.");
        }
        News issueEntity = newsMapper.dtoToEntity(newsRequestTo, author.get());

        try {
            News savedIssue = newsRepository.save(issueEntity);
            return newsMapper.newsToResponseTo(savedIssue);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException(ENTITY_NAME, "iss_content");
        }
    }

    public NewsResponseTo updateIssue(NewsRequestTo newsRequestTo) throws EntityNotFoundException, DuplicateEntityException {
        if (newsRepository.findById(newsRequestTo.getId()).isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, newsRequestTo.getId());
        }

        Optional<Author> author = authorRepository.findById(newsRequestTo.getAuthorId());

        if (author.isEmpty()) {
            throw new EntityNotFoundException("Author", newsRequestTo.getAuthorId());
        }
        if (newsRequestTo.getCreated() == null) {
            newsRequestTo.setCreated(new Date());
        }
        if (newsRequestTo.getModified() == null) {
            newsRequestTo.setModified(new Date());
        }

        News news = newsMapper.dtoToEntity(newsRequestTo, author.get());
        try {
            News savedNews = newsRepository.save(news);
            return newsMapper.newsToResponseTo(savedNews);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException(ENTITY_NAME, "news_content");
        }
    }

    public void deleteIssue(BigInteger id) throws EntityNotFoundException {
        if (newsRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        newsRepository.deleteById(id);
    }
}
