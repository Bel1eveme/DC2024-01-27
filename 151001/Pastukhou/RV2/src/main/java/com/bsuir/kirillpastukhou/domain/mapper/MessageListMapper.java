package com.bsuir.kirillpastukhou.domain.mapper;

import com.bsuir.kirillpastukhou.domain.request.MessageRequestTo;
import com.bsuir.kirillpastukhou.domain.response.MessageResponseTo;
import org.mapstruct.Mapper;
import com.bsuir.kirillpastukhou.domain.entity.Message;

import java.util.List;

@Mapper(componentModel = "spring", uses = MessageMapper.class)
public interface MessageListMapper {
    List<Message> toMessageList(List<MessageRequestTo> messageRequestToList);

    List<MessageResponseTo> toMessageResponseToList(List<Message> messageList);
}
