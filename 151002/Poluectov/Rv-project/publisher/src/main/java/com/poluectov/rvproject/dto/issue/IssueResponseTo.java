package com.poluectov.rvproject.dto.issue;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.poluectov.rvproject.dto.marker.MarkerResponseTo;
import com.poluectov.rvproject.dto.user.UserResponseTo;
import com.poluectov.rvproject.model.IdentifiedEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@JsonRootName("issue")
@AllArgsConstructor
@NoArgsConstructor
public class IssueResponseTo extends IdentifiedEntity {

    private Long id;

    private Long userId;
    private String title;
    private String content;
    private Date created;
    private Date modified;

    private List<Long> markers;
}
