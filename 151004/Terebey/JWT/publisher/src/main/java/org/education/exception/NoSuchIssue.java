package org.education.exception;

public class NoSuchIssue extends RuntimeException{
    public NoSuchIssue(String message) {
        super(message);
    }
}