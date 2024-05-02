package org.education.exception;

public class NoSuchLabel extends RuntimeException{
    public NoSuchLabel(String message) {
        super(message);
    }
}