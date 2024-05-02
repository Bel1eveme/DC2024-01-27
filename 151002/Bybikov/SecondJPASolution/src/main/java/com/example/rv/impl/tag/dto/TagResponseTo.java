package com.example.rv.impl.tag.dto;

import java.math.BigInteger;
import java.util.List;

public record TagResponseTo (
        BigInteger id,
        String name,
        List<BigInteger> tweetIds
){}
