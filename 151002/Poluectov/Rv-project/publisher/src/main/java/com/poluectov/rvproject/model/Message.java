package com.poluectov.rvproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigInteger;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message extends IdentifiedEntity {

    Long id;

    Long issueId;

    String content;

    String country;

}
