package com.poluectov.rvproject.dto.message;

import com.poluectov.rvproject.dto.issue.IssueResponseTo;
import com.poluectov.rvproject.model.IdentifiedEntity;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
