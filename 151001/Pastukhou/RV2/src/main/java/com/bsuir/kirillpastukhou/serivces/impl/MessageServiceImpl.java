package com.bsuir.kirillpastukhou.serivces.impl;

import com.bsuir.kirillpastukhou.domain.entity.Message;
import com.bsuir.kirillpastukhou.domain.entity.Tweet;
import com.bsuir.kirillpastukhou.domain.entity.ValidationMarker;
import com.bsuir.kirillpastukhou.domain.mapper.MessageListMapper;
import com.bsuir.kirillpastukhou.domain.mapper.MessageMapper;
import com.bsuir.kirillpastukhou.domain.request.MessageRequestTo;
import com.bsuir.kirillpastukhou.domain.response.MessageResponseTo;
import com.bsuir.kirillpastukhou.exceptions.NoSuchMessageException;
import com.bsuir.kirillpastukhou.exceptions.NoSuchTweetException;
import com.bsuir.kirillpastukhou.repositories.MessageRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.bsuir.kirillpastukhou.serivces.MessageService;
import com.bsuir.kirillpastukhou.serivces.TweetService;

import java.util.List;

@Service
@Transactional
@Validated
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final TweetService tweetService;
    private final MessageMapper messageMapper;
    private final MessageListMapper messageListMapper;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, TweetService tweetService, MessageMapper messageMapper, MessageListMapper messageListMapper) {
        this.messageRepository = messageRepository;
        this.tweetService = tweetService;
        this.messageMapper = messageMapper;
        this.messageListMapper = messageListMapper;
    }

    @Override
    @Validated(ValidationMarker.OnCreate.class)
    public MessageResponseTo create(@Valid MessageRequestTo entity) {
        Tweet tweet = tweetService.findTweetByIdExt(entity.tweetId()).orElseThrow(() -> new NoSuchTweetException(entity.tweetId()));
        Message message = messageMapper.toMessage(entity);
        message.setTweet(tweet);
        return messageMapper.toMessageResponseTo(messageRepository.save(message));
    }

    @Override
    public List<MessageResponseTo> read() {
        return messageListMapper.toMessageResponseToList(messageRepository.findAll());
    }

    @Override
    @Validated(ValidationMarker.OnUpdate.class)
    public MessageResponseTo update(@Valid MessageRequestTo entity) {
        if (messageRepository.existsById(entity.id())) {
            Message message = messageMapper.toMessage(entity);
            Tweet tweetRef = tweetService.findTweetByIdExt(message.getTweet().getId()).orElseThrow(() -> new NoSuchTweetException(message.getTweet().getId()));
            message.setTweet(tweetRef);
            return messageMapper.toMessageResponseTo(messageRepository.save(message));
        } else {
            throw new NoSuchMessageException(entity.id());
        }
    }

    @Override
    public void delete(Long id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
        } else {
            throw new NoSuchMessageException(id);
        }
    }

    @Override
    public MessageResponseTo findMessageById(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(() -> new NoSuchMessageException(id));
        return messageMapper.toMessageResponseTo(message);
    }
}
