package org.example.publisher.impl.tweet.dto;

import java.math.BigInteger;
import java.util.Date;

public record TweetResponseTo (
        BigInteger id,
        BigInteger editorId,
        String title,
        String content,
        Date created,
        Date modified
){

}
