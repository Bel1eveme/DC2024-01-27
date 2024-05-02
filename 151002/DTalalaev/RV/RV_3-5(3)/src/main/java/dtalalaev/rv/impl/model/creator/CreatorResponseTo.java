package dtalalaev.rv.impl.model.creator;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreatorResponseTo {
    private BigInteger id;
    @Size(min = 3, max = 20, message = "Login must be between 3 and 30 characters")
    private String login;
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    private String password;
    @Size(min = 2, max = 64, message = "Firstname must be between 2 and 64 characters")
    private String firstname;
    @Size(min = 2, max = 64, message = "Lastname must be between 2 and 64 characters")
    private String lastname;
}