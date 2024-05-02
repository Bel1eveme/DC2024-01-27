package com.poluectov.rvproject.dto.message;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.poluectov.rvproject.dto.issue.IssueResponseTo;
import com.poluectov.rvproject.model.IdentifiedEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

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
}
