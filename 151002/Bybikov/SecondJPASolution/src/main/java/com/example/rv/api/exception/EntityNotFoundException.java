package com.example.rv.api.exception;

import java.math.BigInteger;

public class EntityNotFoundException extends Exception {

    public EntityNotFoundException(String entity, BigInteger id){
        super(entity + "with ids " + id + " not found");
    }

}
