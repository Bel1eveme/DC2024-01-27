package org.example.publisher.impl.author.dto;

import java.math.BigInteger;

public record AuthorResponseTo(
        BigInteger id,
        String login,
        String firstname,
        String lastname
){}