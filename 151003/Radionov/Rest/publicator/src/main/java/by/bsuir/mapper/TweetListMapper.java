package by.bsuir.mapper;

import by.bsuir.dto.TweetRequestTo;
import by.bsuir.dto.TweetResponseTo;
import by.bsuir.entities.Tweet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = TweetMapper.class)
public interface TweetListMapper {
    List<Tweet> toTweetList(List<TweetRequestTo> tweets);
    List<TweetResponseTo> toTweetResponseList(List<Tweet> tweets);
}
