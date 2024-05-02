package dtalalaev.rv.impl.model.tag;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
@Entity
@Getter
@Setter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;
    private String name;

    public void setName(String name) {
        if( name == null || name.length() < 2 || name.length() > 32){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect length of name");
        }
        this.name = name;
    }
}


