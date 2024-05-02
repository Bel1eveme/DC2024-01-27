package com.bsuir.nastassiayankova.mappers;

import com.bsuir.nastassiayankova.beans.entity.News;
import com.bsuir.nastassiayankova.beans.request.NewsRequestTo;
import com.bsuir.nastassiayankova.beans.response.NewsResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MessageListMapper.class, TagListMapper.class, UserMapper.class})
public interface NewsMapper {
    @Mapping(source = "userId", target = "user.id")
    News toNews(NewsRequestTo newsRequestTo);
    @Mapping(source = "user.id", target = "userId")
    NewsResponseTo toNewsResponseTo(News news);
}
