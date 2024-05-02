package com.bsuir.kirillpastukhou.serivces.impl;

import com.bsuir.kirillpastukhou.domain.entity.Creator;
import com.bsuir.kirillpastukhou.domain.entity.Tweet;
import com.bsuir.kirillpastukhou.domain.entity.ValidationMarker;
import com.bsuir.kirillpastukhou.domain.mapper.TweetListMapper;
import com.bsuir.kirillpastukhou.domain.mapper.TweetMapper;
import com.bsuir.kirillpastukhou.domain.request.TweetRequestTo;
import com.bsuir.kirillpastukhou.domain.response.TweetResponseTo;
import com.bsuir.kirillpastukhou.exceptions.NoSuchCreatorException;
import com.bsuir.kirillpastukhou.exceptions.NoSuchTweetException;
import com.bsuir.kirillpastukhou.serivces.TweetService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.bsuir.kirillpastukhou.repositories.TweetRepository;
import com.bsuir.kirillpastukhou.serivces.CreatorService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Validated
public class TweetServiceImpl implements TweetService {
    private final CreatorService creatorService;
    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;
    private final TweetListMapper tweetListMapper;

    @Autowired
    public TweetServiceImpl(CreatorService creatorService, TweetRepository tweetRepository, TweetMapper tweetMapper, TweetListMapper tweetListMapper) {
        this.creatorService = creatorService;
        this.tweetRepository = tweetRepository;
        this.tweetMapper = tweetMapper;
        this.tweetListMapper = tweetListMapper;
    }

    @Override
    @Validated(ValidationMarker.OnCreate.class)
    public TweetResponseTo create(@Valid TweetRequestTo entity) {
        Creator creator = creatorService.findCreatorByIdExt(entity.creatorId()).orElseThrow(() -> new NoSuchCreatorException(entity.creatorId()));
        Tweet tweet = tweetMapper.toTweet(entity);
        tweet.setCreator(creator);
        return tweetMapper.toTweetResponseTo(tweetRepository.save(tweet));
    }

    @Override
    public List<TweetResponseTo> read() {
        return tweetListMapper.toTweetResponseToList(tweetRepository.findAll());
    }

    @Override
    @Validated(ValidationMarker.OnUpdate.class)
    public TweetResponseTo update(@Valid TweetRequestTo entity) {
        if (tweetRepository.existsById(entity.id())) {
            Tweet tweet = tweetMapper.toTweet(entity);
            Tweet tweetRef = tweetRepository.getReferenceById(tweet.getId());
            tweet.setCreator(tweetRef.getCreator());
            tweet.setMessageList(tweetRef.getMessageList());
            tweet.setTweetTagList(tweetRef.getTweetTagList());
            //  tweetResponseTo.stickerList() = tweet.getTweetTagList().stream().map(element -> stickerMapper.toTagResponseTo(element.getTag())).collect(Collectors.toList());
            return tweetMapper.toTweetResponseTo(tweetRepository.save(tweet));
        } else {
            throw new NoSuchCreatorException(entity.id());
        }
    }

    @Override
    public void delete(Long id) {
        if (tweetRepository.existsById(id)) {
            tweetRepository.deleteById(id);
        } else {
            throw new NoSuchTweetException(id);
        }

    }

    @Override
    public TweetResponseTo findTweetById(Long id) {
        Tweet tweet = tweetRepository.findById(id).orElseThrow(() -> new NoSuchTweetException(id));
        return tweetMapper.toTweetResponseTo(tweet);
    }

    @Override
    public Optional<Tweet> findTweetByIdExt(Long id) {
        return tweetRepository.findById(id);
    }
}
