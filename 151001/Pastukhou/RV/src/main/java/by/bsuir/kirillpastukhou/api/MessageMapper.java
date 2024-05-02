package by.bsuir.kirillpastukhou.api;

import by.bsuir.kirillpastukhou.impl.bean.Message;
import by.bsuir.kirillpastukhou.impl.dto.MessageRequestTo;
import by.bsuir.kirillpastukhou.impl.dto.MessageResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper( MessageMapper.class );

    Message MessageResponseToToMessage(MessageResponseTo messageResponseTo);
    Message MessageRequestToToMessage(MessageRequestTo messageRequestTo);

    MessageResponseTo MessageToMessageResponseTo(Message message);

    MessageRequestTo MessageToMessageRequestTo(Message message);
}
