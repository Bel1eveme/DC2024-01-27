package by.bsuir.romankokarev.mapper;

import by.bsuir.romankokarev.dto.MessageRequestTo;
import by.bsuir.romankokarev.dto.MessageResponseTo;
import by.bsuir.romankokarev.model.Message;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring", uses = MessageMapper.class)
public interface MessageListMapper {
    List<Message> toMessageList(List<MessageRequestTo> messageRequestToList);

    List<MessageResponseTo> toMessageResponseList(List<Message> messageList);
}
