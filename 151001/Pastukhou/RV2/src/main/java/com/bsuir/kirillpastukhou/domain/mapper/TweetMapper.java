package com.bsuir.kirillpastukhou.domain.mapper;

import com.bsuir.kirillpastukhou.domain.entity.Tweet;
import com.bsuir.kirillpastukhou.domain.request.TweetRequestTo;
import com.bsuir.kirillpastukhou.domain.response.TweetResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MessageListMapper.class, TagListMapper.class, CreatorMapper.class})
public interface TweetMapper {
    @Mapping(source = "creatorId", target = "creator.id")
    Tweet toTweet(TweetRequestTo tweetRequestTo);
    @Mapping(source = "creator.id", target = "creatorId")
    TweetResponseTo toTweetResponseTo(Tweet tweet);
}
