package com.bsuir.kirillpastukhou.exceptions;

public class NoSuchTagException extends IllegalArgumentException{
    private final Long tagId;

    public NoSuchTagException(Long tagId) {
        super(String.format("Tag with id %d is not found in DB", tagId));
        this.tagId = tagId;
    }
}
