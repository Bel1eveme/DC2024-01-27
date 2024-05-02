package org.example.publisher.impl.news.mapper;

import org.example.publisher.impl.author.Author;
import org.example.publisher.impl.news.News;
import org.example.publisher.impl.news.dto.NewsRequestTo;
import org.example.publisher.impl.news.dto.NewsResponseTo;

import java.util.List;

public interface NewsMapper {

    NewsRequestTo newsToRequestTo(News news);

    List<NewsRequestTo> newsToRequestTo(Iterable<News> news);

    News dtoToEntity(NewsRequestTo newsRequestTo, Author author);

    NewsResponseTo newsToResponseTo(News news);

    List<NewsResponseTo> newsToResponseTo(Iterable<News> news);
}
