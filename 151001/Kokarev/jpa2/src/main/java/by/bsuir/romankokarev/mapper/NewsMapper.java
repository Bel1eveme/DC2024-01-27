package by.bsuir.romankokarev.mapper;

import by.bsuir.romankokarev.dto.NewsRequestTo;
import by.bsuir.romankokarev.dto.NewsResponseTo;
import by.bsuir.romankokarev.model.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    News newsRequestToNews(NewsRequestTo newsRequestTo);

    @Mapping(target = "userId", source = "news.user.id")
    NewsResponseTo newsToNewsResponse(News news);
}
