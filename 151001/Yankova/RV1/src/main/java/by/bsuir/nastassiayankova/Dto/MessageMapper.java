package by.bsuir.nastassiayankova.Dto;

import by.bsuir.nastassiayankova.Entity.Message;
import by.bsuir.nastassiayankova.Dto.impl.MessageRequestTo;
import by.bsuir.nastassiayankova.Dto.impl.MessageResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    Message MessageResponseToToMessage(MessageResponseTo messageResponseTo);

    Message MessageRequestToToMessage(MessageRequestTo messageRequestTo);

    MessageResponseTo MessageToMessageResponseTo(Message message);

    MessageRequestTo MessageToMessageRequestTo(Message message);
}
