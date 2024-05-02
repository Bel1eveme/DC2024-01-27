package com.poluectov.rvproject.dto.marker;

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
public class MarkerRequestTo extends IdentifiedEntity {

    private Long id;
    @Size(min = 2, max = 32)
    private String name;

}
