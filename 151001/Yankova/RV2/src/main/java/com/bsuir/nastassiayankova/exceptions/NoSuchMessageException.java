package com.bsuir.nastassiayankova.exceptions;

public class NoSuchMessageException extends IllegalArgumentException {
    private final Long messageId;

    public NoSuchMessageException(Long messageId) {
        super(String.format("User with id %d is not found in DB", messageId));
        this.messageId = messageId;
    }
}
