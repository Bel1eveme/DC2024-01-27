package org.example.publisher.impl.news.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
public class NewsRequestTo {
        BigInteger id;
        BigInteger authorId;

        @Size(min = 2, max = 64)
        String title;

        @Size(min = 4, max = 2048)
        String content;
        Date created;
        Date modified;
}
