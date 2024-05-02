package by.bsuir.nastassiayankova.Dto;

import by.bsuir.nastassiayankova.Entity.News;
import by.bsuir.nastassiayankova.Dto.impl.NewsRequestTo;
import by.bsuir.nastassiayankova.Dto.impl.NewsResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NewsMapper {
    NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);


    NewsRequestTo NewsToNewsRequestTo(News news);

    NewsResponseTo NewsToNewsResponseTo(News news);

    News NewsResponseToToNews(NewsResponseTo responseTo);

    News NewsRequestToToNews(NewsRequestTo requestTo);
}
