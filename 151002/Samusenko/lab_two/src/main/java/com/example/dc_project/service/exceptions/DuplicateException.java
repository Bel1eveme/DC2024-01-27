package com.example.dc_project.service.exceptions;

import com.example.dc_project.model.response.ErrorResponseTo;

public class DuplicateException extends ResourceException{
    public DuplicateException(int code, String message)
    {
        super(code, message);
    }
}
