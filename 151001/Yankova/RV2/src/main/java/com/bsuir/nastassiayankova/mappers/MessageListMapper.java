package com.bsuir.nastassiayankova.mappers;

import com.bsuir.nastassiayankova.beans.request.MessageRequestTo;
import com.bsuir.nastassiayankova.beans.response.MessageResponseTo;
import org.mapstruct.Mapper;
import com.bsuir.nastassiayankova.beans.entity.Message;

import java.util.List;

@Mapper(componentModel = "spring", uses = MessageMapper.class)
public interface MessageListMapper {
    List<Message> toMessageList(List<MessageRequestTo> messageRequestToList);

    List<MessageResponseTo> toMessageResponseToList(List<Message> messageList);
}
