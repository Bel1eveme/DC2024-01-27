package com.bsuir.nastassiayankova.exceptions;

public class NoSuchNewsException extends IllegalArgumentException {
    private final Long newsId;

    public NoSuchNewsException(Long newsId) {
        super(String.format("News with id %d is not found in DB", newsId));
        this.newsId = newsId;
    }
}
