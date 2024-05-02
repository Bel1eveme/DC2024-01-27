package com.poluectov.reproject.discussion.dto.message;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.poluectov.reproject.discussion.model.IdentifiedEntity;
import lombok.*;

import java.math.BigInteger;
import java.util.function.LongBinaryOperator;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@JsonRootName("message")
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseTo extends IdentifiedEntity {

    private Long id;
    private Long issueId;
    private String content;
    private String country;
}
