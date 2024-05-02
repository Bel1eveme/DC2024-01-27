package com.poluectov.reproject.discussion.dto.message;

import com.poluectov.reproject.discussion.model.IdentifiedEntity;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigInteger;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestTo extends IdentifiedEntity {

    private Long id;
    private Long issueId;
    @Size(min = 2, max = 2048)
    private String content;

    private String country;

}
