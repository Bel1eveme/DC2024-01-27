package by.bsuir.discussionservice.mapper;

import by.bsuir.discussionservice.dto.request.MessageRequestTo;
import by.bsuir.discussionservice.dto.response.MessageResponseTo;
import by.bsuir.discussionservice.entity.Message;
import by.bsuir.discussionservice.entity.MessageState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "key", expression = "java(new Message.Key(request.country(), request.storyId(), request.id()))")
    Message toEntity(MessageRequestTo request);

    @Mapping(target = "id", source = "key.id")
    @Mapping(target = "storyId", source = "key.storyId")
    @Mapping(target = "state", expression = "java(MessageState.APPROVED)")
    MessageResponseTo toResponseTo(Message entity);

    @Mapping(target = "id", source = "entity.key.id")
    @Mapping(target = "storyId", source = "entity.key.storyId")
    MessageResponseTo toResponseTo(Message entity, MessageState state);

    @Mapping(target = "key.id", ignore = true)
    @Mapping(target = "key.storyId", source = "storyId")
    @Mapping(target = "key.country", source = "country")
    Message updateEntity(@MappingTarget Message entity, MessageRequestTo request);
}
