package com.example.dc_project.service.exceptions;

public class ResourceStateException extends ResourceException{
    public ResourceStateException(int code, String message) {
        super(code, message);
    }
}
