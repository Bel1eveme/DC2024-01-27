package com.poluectov.rvproject.dto.user;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.poluectov.rvproject.model.IdentifiedEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@JsonRootName("user")
public class UserResponseTo extends IdentifiedEntity {

    private Long id;
    private String login;
    private String hashedPassword;
    private String firstname;
    private String lastname;

}