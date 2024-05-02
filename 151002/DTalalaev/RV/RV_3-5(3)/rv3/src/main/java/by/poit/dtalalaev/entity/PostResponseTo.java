package by.poit.dtalalaev.entity;

import lombok.*;

import javax.validation.constraints.Size;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseTo {
    private BigInteger id;
    private BigInteger storyId;
    @Size(min =2 , max = 2048, message = "Content must be between 2 and 2048 characters")
    private String content;

    public PostResponseTo(Post post) {
        this.id = post.getId();
        this.storyId = post.getStoryId();
        this.content = post.getContent();
    }
}
