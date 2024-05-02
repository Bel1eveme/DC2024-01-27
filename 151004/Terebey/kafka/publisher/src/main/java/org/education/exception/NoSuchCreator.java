package org.education.exception;

public class NoSuchCreator extends RuntimeException{
    public NoSuchCreator(String message) {
        super(message);
    }
}
