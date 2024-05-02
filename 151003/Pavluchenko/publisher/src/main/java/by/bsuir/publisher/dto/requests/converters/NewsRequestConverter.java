package by.bsuir.publisher.dto.requests.converters;

import by.bsuir.publisher.domain.News;
import by.bsuir.publisher.dto.requests.NewsRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NewsRequestConverter {
    @Mapping(source = "authorId", target = "author.id")
    News fromDto(NewsRequestDto news);
}
