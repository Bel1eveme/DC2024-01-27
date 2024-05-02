package com.example.rv.impl.tweet.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
public class TweetRequestTo {
        BigInteger id;
        BigInteger editorId;

        @Size(min = 2, max = 64)
        String title;

        @Size(min = 4, max = 2048)
        String content;
        Date created;
        Date modified;
}
