package by.poit.dtalalaev.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;

@Getter
@Data
@Table("tbl_posts")
@Setter
public class Post {
    @PrimaryKey
    private BigInteger id;

    @Column("story_id")
    private BigInteger storyId;
    private String content;

    public void setContent(String content) throws ResponseStatusException {
        if(content == null || content.length() < 4 || content.length() > 2048){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect length of content");
        }
        this.content = content;
    }

}
