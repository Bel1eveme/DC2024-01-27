package com.example.dc_project.service.exceptions;

public class ResourceNotFoundException extends ResourceException{
    public ResourceNotFoundException(int code, String message) {
        super(code, message);
    }
}
