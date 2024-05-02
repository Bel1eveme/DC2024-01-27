package dtalalaev.rv.impl.model.creator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;

@Getter
@Entity
@Setter
public class Creator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;
    @Column(unique = true)
    private String login;
    private String password;
    private String firstname;
    private String lastname;

    public void setLogin(String login) throws ResponseStatusException {
        if(login == null || login.length() < 3 || login.length() > 20){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect length of login");
        }
        this.login = login;
    }

    public void setPassword(String password) throws ResponseStatusException {
        if(password == null || password.length() < 8 || password.length() > 128){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect length of password");
        }
        this.password = password;
    }

    public void setFirstname(String firstname) throws ResponseStatusException {
        if(firstname == null || firstname.length() < 2 || firstname.length() > 64){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect length of firstname");
        }
        this.firstname = firstname;
    }

    public void setLastname(String lastname) throws ResponseStatusException {
        if(lastname == null || lastname.length() < 2 || lastname.length() > 64){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect length of lastname");
        }
        this.lastname = lastname;
    }

}
