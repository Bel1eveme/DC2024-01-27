package by.bsuir.kirillpastukhou.impl.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
public class TweetRequestTo {
    private long id;
    private long creatorId;
    private String title;
    private String content;
    private Date created;
    private Date modified;

}
