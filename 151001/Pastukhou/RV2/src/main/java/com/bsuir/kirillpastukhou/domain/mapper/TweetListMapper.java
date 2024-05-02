package com.bsuir.kirillpastukhou.domain.mapper;

import com.bsuir.kirillpastukhou.domain.entity.Tweet;
import com.bsuir.kirillpastukhou.domain.request.TweetRequestTo;
import com.bsuir.kirillpastukhou.domain.response.TweetResponseTo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = TweetMapper.class)
public interface TweetListMapper {
    List<Tweet> toTweetList(List<TweetRequestTo> tweetRequestToList);

    List<TweetResponseTo> toTweetResponseToList(List<Tweet> tweetList);
}
