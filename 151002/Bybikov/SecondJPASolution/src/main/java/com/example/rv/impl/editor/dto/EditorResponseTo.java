package com.example.rv.impl.editor.dto;

import java.math.BigInteger;

public record EditorResponseTo(
        BigInteger id,
        String login,
        String firstname,
        String lastname
){}