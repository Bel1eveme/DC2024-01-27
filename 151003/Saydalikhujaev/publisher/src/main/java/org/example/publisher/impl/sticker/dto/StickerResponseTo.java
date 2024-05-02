package org.example.publisher.impl.sticker.dto;

import java.math.BigInteger;
import java.util.List;

public record StickerResponseTo(
        BigInteger id,
        String name,
        List<BigInteger> issueIds
){}
