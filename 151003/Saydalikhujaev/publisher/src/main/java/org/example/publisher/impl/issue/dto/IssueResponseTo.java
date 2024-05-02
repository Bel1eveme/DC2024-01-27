package org.example.publisher.impl.issue.dto;

import java.math.BigInteger;
import java.util.Date;

public record IssueResponseTo(
        BigInteger id,
        BigInteger creatorId,
        String title,
        String content,
        Date created,
        Date modified
){

}


