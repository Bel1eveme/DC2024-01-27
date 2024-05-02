package com.bsuir.nastassiayankova.exceptions;

public class NoSuchUserException extends IllegalArgumentException {
    private final Long userId;

    public NoSuchUserException(Long userId) {
        super(String.format("User with id %d is not found in DB", userId));
        this.userId = userId;
    }
}
