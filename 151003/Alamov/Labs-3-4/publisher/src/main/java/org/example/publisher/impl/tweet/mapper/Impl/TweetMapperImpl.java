package org.example.publisher.impl.tweet.mapper.Impl;

import org.example.publisher.impl.editor.Editor;
import org.example.publisher.impl.tweet.Tweet;
import org.example.publisher.impl.tweet.dto.TweetRequestTo;
import org.example.publisher.impl.tweet.dto.TweetResponseTo;
import org.example.publisher.impl.tweet.mapper.TweetMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TweetMapperImpl implements TweetMapper {
    @Override
    public TweetRequestTo tweetToRequestTo(Tweet tweet) {
        return new TweetRequestTo(
                tweet.getId(),
                tweet.getEditor().getId(),
                tweet.getTitle(),
                tweet.getContent(),
                tweet.getCreated(),
                tweet.getModified()
        );
    }

    @Override
    public List<TweetRequestTo> tweetToRequestTo(Iterable<Tweet> tweets) {
        return StreamSupport.stream(tweets.spliterator(), false)
                .map(this::tweetToRequestTo)
                .collect(Collectors.toList());
    }

    @Override
    public Tweet dtoToEntity(TweetRequestTo tweetRequestTo, Editor editor) {
        return new Tweet(
                tweetRequestTo.getId(),
                editor,
                tweetRequestTo.getTitle(),
                tweetRequestTo.getContent(),
                tweetRequestTo.getModified(),
                tweetRequestTo.getCreated());
    }

    @Override
    public TweetResponseTo tweetToResponseTo(Tweet tweet) {
        return new TweetResponseTo(
                tweet.getId(),
                tweet.getEditor().getId(),
                tweet.getTitle(),
                tweet.getContent(),
                tweet.getCreated(),
                tweet.getModified()
        );
    }

    @Override
    public List<TweetResponseTo> tweetToResponseTo(Iterable<Tweet> tweets) {
        return StreamSupport.stream(tweets.spliterator(), false)
                .map(this::tweetToResponseTo)
                .collect(Collectors.toList());
    }
}
