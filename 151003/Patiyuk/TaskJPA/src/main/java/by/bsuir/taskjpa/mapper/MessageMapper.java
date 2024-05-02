package by.bsuir.taskrest.mapper;

import by.bsuir.taskrest.dto.request.MessageRequestTo;
import by.bsuir.taskrest.dto.response.MessageResponseTo;
import by.bsuir.taskrest.entity.Message;
import by.bsuir.taskrest.entity.Story;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "story", expression = "java(story)")
    Message toEntity(MessageRequestTo request, @Context Story story);

    @Mapping(target = "storyId", source = "story.id")
    MessageResponseTo toResponseTo(Message entity);

    Message updateEntity(@MappingTarget Message entity, MessageRequestTo request);
}
