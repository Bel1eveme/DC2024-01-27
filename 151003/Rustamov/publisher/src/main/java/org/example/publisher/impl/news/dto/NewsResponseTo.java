package org.example.publisher.impl.news.dto;

import java.math.BigInteger;
import java.util.Date;

public record NewsResponseTo(
        BigInteger id,
        BigInteger authorId,
        String title,
        String content,
        Date created,
        Date modified
){

}
