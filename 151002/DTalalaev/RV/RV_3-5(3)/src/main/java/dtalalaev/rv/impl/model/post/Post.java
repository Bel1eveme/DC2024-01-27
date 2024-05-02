package dtalalaev.rv.impl.model.post;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Size;
import java.math.BigInteger;


@Getter
@Entity
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;
    private BigInteger storyId;
    private String content;

    public void setContent(String content) throws ResponseStatusException {
        if(content == null || content.length() < 4 || content.length() > 2048){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect length of content");
        }
        this.content = content;
    }

}
