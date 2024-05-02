package org.education.bean.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageResponseTo {

    @JsonAlias("id")
    int id;
    int issueId;
    String content;

    @JsonGetter("id")
    public int getId() {
        return id;
    }
}