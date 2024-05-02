package by.bsuir.publisherservice.client.discussion.mapper;

import by.bsuir.publisherservice.client.discussion.request.DiscussionMessageRequestTo;
import by.bsuir.publisherservice.client.discussion.response.DiscussionMessageResponseTo;
import by.bsuir.publisherservice.dto.request.MessageRequestTo;
import by.bsuir.publisherservice.dto.response.MessageResponseTo;
import by.bsuir.publisherservice.entity.MessageState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiscussionMessageMapper {
    DiscussionMessageRequestTo toDiscussionRequest(MessageRequestTo requestTo, String country);

    @Mapping(target = "id", source = "id")
    DiscussionMessageRequestTo toDiscussionRequest(MessageRequestTo requestTo, Long id, String country);

    MessageResponseTo toMessageResponse(DiscussionMessageResponseTo responseTo);

    DiscussionMessageResponseTo toDiscussionResponse(DiscussionMessageRequestTo requestTo, MessageState state);
}
