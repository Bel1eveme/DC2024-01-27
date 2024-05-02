package com.poluectov.rvproject.dto.user;

import com.poluectov.rvproject.model.IdentifiedEntity;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigInteger;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestTo extends IdentifiedEntity {

    private Long id;
    @Size(min = 2, max = 64)
    String login;
    @Size(min = 8, max = 128)
    String password;
    @Size(min = 2, max = 64)
    String firstname;
    @Size(min = 2, max = 64)
    String lastname;
}
