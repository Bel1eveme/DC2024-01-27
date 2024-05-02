package com.poluectov.rvproject.dto.marker;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.poluectov.rvproject.model.IdentifiedEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@JsonRootName("marker")
@AllArgsConstructor
@NoArgsConstructor
public class MarkerResponseTo extends IdentifiedEntity {

    private Long id;
    private String name;

}
