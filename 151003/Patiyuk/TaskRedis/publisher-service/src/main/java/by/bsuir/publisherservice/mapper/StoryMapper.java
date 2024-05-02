package by.bsuir.publisherservice.mapper;

import by.bsuir.publisherservice.dto.request.StoryRequestTo;
import by.bsuir.publisherservice.dto.response.StoryResponseTo;
import by.bsuir.publisherservice.entity.Creator;
import by.bsuir.publisherservice.entity.Story;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creator", expression = "java(creator)")
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "modified", expression = "java(java.time.LocalDateTime.now())")
    Story toEntity(StoryRequestTo request, @Context Creator creator);

    @Mapping(target = "creatorId", source = "creator.id")
    StoryResponseTo toResponseTo(Story entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "modified", expression = "java(java.time.LocalDateTime.now())")
    Story updateEntity(@MappingTarget Story entity, StoryRequestTo request);
}
