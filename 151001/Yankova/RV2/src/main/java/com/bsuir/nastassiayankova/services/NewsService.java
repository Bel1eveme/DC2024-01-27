package com.bsuir.nastassiayankova.services;

import com.bsuir.nastassiayankova.beans.entity.News;
import com.bsuir.nastassiayankova.beans.entity.ValidationMarker;
import com.bsuir.nastassiayankova.beans.request.NewsRequestTo;
import com.bsuir.nastassiayankova.beans.response.NewsResponseTo;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

public interface NewsService {
    @Validated(ValidationMarker.OnCreate.class)
    NewsResponseTo create(@Valid NewsRequestTo entity);

    List<NewsResponseTo> read();

    @Validated(ValidationMarker.OnUpdate.class)
    NewsResponseTo update(@Valid NewsRequestTo entity);

    void delete(Long id);

    NewsResponseTo findNewsById(Long id);

    Optional<News> findNewsByIdExt(Long id);
}
