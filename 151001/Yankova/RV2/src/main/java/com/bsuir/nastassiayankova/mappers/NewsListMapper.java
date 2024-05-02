package com.bsuir.nastassiayankova.mappers;

import com.bsuir.nastassiayankova.beans.entity.News;
import com.bsuir.nastassiayankova.beans.request.NewsRequestTo;
import com.bsuir.nastassiayankova.beans.response.NewsResponseTo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = NewsMapper.class)
public interface NewsListMapper {
    List<News> toNewsList(List<NewsRequestTo> newsRequestToList);

    List<NewsResponseTo> toNewsResponseToList(List<News> newsList);
}
