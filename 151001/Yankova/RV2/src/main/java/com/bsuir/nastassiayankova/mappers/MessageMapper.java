package com.bsuir.nastassiayankova.mappers;

import com.bsuir.nastassiayankova.beans.entity.Message;
import com.bsuir.nastassiayankova.beans.request.MessageRequestTo;
import com.bsuir.nastassiayankova.beans.response.MessageResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = NewsMapper.class)
public interface MessageMapper {
    @Mapping(source = "newsId", target = "news.id")
    Message toMessage(MessageRequestTo messageRequestTo);
    @Mapping(source = "news.id", target = "newsId")
    MessageResponseTo toMessageResponseTo(Message message);
}
