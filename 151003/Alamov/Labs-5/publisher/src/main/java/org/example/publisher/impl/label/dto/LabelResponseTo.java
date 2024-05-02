package org.example.publisher.impl.label.dto;

import java.math.BigInteger;
import java.util.List;

public record LabelResponseTo (
        BigInteger id,
        String name,
        List<BigInteger> tweetIds
){}
