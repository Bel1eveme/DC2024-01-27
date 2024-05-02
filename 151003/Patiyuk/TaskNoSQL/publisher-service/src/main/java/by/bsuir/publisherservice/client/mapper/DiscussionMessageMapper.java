package by.bsuir.publisherservice.client.mapper;

import by.bsuir.publisherservice.client.request.DiscussionMessageRequestTo;
import by.bsuir.publisherservice.client.response.DiscussionMessageResponseTo;
import by.bsuir.publisherservice.dto.request.MessageRequestTo;
import by.bsuir.publisherservice.dto.response.MessageResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiscussionMessageMapper {
    DiscussionMessageRequestTo toDiscussionRequest(MessageRequestTo requestTo, String country);
    @Mapping(target = "id", source = "id")
    DiscussionMessageRequestTo toDiscussionRequest(MessageRequestTo requestTo, Long id, String country);
    MessageResponseTo toMessageResponse(DiscussionMessageResponseTo responseTo);
}
