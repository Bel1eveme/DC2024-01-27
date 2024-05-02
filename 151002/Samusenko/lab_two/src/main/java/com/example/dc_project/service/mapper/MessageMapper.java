package com.example.dc_project.service.mapper;

import com.example.dc_project.model.entity.Message;
import com.example.dc_project.model.request.MessageRequestTo;
import com.example.dc_project.model.response.MessageResponseTo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageResponseTo getResponse(Message message);
    List<MessageResponseTo> getListResponse(Iterable<Message> messages);
    Message getMessage(MessageRequestTo messageRequestTo);
}
