package dtalalaev.rv.impl.model.story;

import dtalalaev.rv.impl.model.creator.CreatorRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.Date;


@Getter
@Setter
@Entity
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;
    private BigInteger creatorId;
    @Column(unique = true)
    private String title;
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @PrePersist
    protected void onCreate() {

        created = new Date();
        modified = (Date) created.clone();
    }
    private Date modified;

    public void setTitle(String title) throws ResponseStatusException {
        if(title == null || title.length() < 2 || title.length() > 64){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect length of title");
        }
        this.title = title;
    }

    public void setContent(String content) throws ResponseStatusException {
        if(content == null || content.length() < 4 || content.length() > 2048){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect length of content");
        }
        this.content = content;
    }

}
