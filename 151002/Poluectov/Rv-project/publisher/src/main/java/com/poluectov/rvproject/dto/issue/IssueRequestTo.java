package com.poluectov.rvproject.dto.issue;

import com.poluectov.rvproject.model.IdentifiedEntity;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class IssueRequestTo extends IdentifiedEntity {

    private Long id;

    private Long userId;
    @Size(min = 2, max = 64)
    private String title;
    @Size(min = 4, max = 2048)
    private String content;
    private Date created;
    private Date modified;

    private List<Long> markers;
}
