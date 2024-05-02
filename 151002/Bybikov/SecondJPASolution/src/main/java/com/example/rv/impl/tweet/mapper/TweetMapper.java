package com.example.rv.impl.tweet.mapper;

import com.example.rv.impl.editor.Editor;
import com.example.rv.impl.tweet.Tweet;
import com.example.rv.impl.tweet.dto.TweetRequestTo;
import com.example.rv.impl.tweet.dto.TweetResponseTo;

import java.util.List;

public interface TweetMapper {

    TweetRequestTo tweetToRequestTo(Tweet tweet);

    List<TweetRequestTo> tweetToRequestTo(Iterable<Tweet> tweets);

    Tweet dtoToEntity(TweetRequestTo tweetRequestTo, Editor editor);

    TweetResponseTo tweetToResponseTo(Tweet tweet);

    List<TweetResponseTo> tweetToResponseTo(Iterable<Tweet> tweets);
}
