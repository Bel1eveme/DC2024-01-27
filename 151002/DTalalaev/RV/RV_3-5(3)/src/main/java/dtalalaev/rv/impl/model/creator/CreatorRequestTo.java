package dtalalaev.rv.impl.model.creator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Size;
import java.math.BigInteger;

@Getter
@Builder
public class CreatorRequestTo {
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
