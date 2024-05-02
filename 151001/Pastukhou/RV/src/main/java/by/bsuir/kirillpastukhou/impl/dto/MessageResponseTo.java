package by.bsuir.kirillpastukhou.impl.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageResponseTo {
    private long id;
    private String content;
    private long tweetId;
}
