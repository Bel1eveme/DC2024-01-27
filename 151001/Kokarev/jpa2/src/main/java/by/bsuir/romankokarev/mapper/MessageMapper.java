package by.bsuir.romankokarev.mapper;

import by.bsuir.romankokarev.dto.MessageRequestTo;
import by.bsuir.romankokarev.dto.MessageResponseTo;
import by.bsuir.romankokarev.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message messageRequestToMessage(MessageRequestTo messageRequestTo);

    @Mapping(target = "newsId", source = "message.news.id")
    MessageResponseTo messageToMessageResponse(Message message);
}
