package com.bsuir.kirillpastukhou.domain.mapper;

import com.bsuir.kirillpastukhou.domain.entity.Message;
import com.bsuir.kirillpastukhou.domain.request.MessageRequestTo;
import com.bsuir.kirillpastukhou.domain.response.MessageResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TweetMapper.class)
public interface MessageMapper {
    @Mapping(source = "tweetId", target = "tweet.id")
    Message toMessage(MessageRequestTo messageRequestTo);
    @Mapping(source = "tweet.id", target = "tweetId")
    MessageResponseTo toMessageResponseTo(Message message);
}
