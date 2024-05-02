package by.bsuir.kirillpastukhou.api;

import by.bsuir.kirillpastukhou.impl.bean.Tweet;
import by.bsuir.kirillpastukhou.impl.dto.TweetRequestTo;
import by.bsuir.kirillpastukhou.impl.dto.TweetResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TweetMapper {
    TweetMapper INSTANCE = Mappers.getMapper(TweetMapper.class);


    TweetRequestTo TweetToTweetRequestTo(Tweet tweet);

    TweetResponseTo TweetToTweetResponseTo(Tweet tweet);

    Tweet TweetResponseToToTweet(TweetResponseTo responseTo);

    Tweet TweetRequestToToTweet(TweetRequestTo requestTo);
}
