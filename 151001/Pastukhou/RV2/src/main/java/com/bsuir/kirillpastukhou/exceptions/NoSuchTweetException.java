package com.bsuir.kirillpastukhou.exceptions;

public class NoSuchTweetException extends IllegalArgumentException {
    private final Long tweetId;

    public NoSuchTweetException(Long tweetId) {
        super(String.format("Tweet with id %d is not found in DB", tweetId));
        this.tweetId = tweetId;
    }
}
