package org.example.publisher.impl.creator.dto;

import java.math.BigInteger;

public record CreatorResponseTo(
        BigInteger id,
        String login,
        String firstname,
        String lastname
){}