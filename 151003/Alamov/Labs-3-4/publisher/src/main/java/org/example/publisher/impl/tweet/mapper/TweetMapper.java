package org.example.publisher.impl.tweet.mapper;

import org.example.publisher.impl.editor.Editor;
import org.example.publisher.impl.tweet.Tweet;
import org.example.publisher.impl.tweet.dto.TweetRequestTo;
import org.example.publisher.impl.tweet.dto.TweetResponseTo;

import java.util.List;

public interface TweetMapper {

    TweetRequestTo tweetToRequestTo(Tweet tweet);

    List<TweetRequestTo> tweetToRequestTo(Iterable<Tweet> tweets);

    Tweet dtoToEntity(TweetRequestTo tweetRequestTo, Editor editor);

    TweetResponseTo tweetToResponseTo(Tweet tweet);

    List<TweetResponseTo> tweetToResponseTo(Iterable<Tweet> tweets);
}
