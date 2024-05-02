package org.education.bean.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageRequestTo {
    @JsonAlias("id")
    int id;

    @NotNull
    @Min(value = 0)
    Integer issueId;

    @NotNull
    @Size(min = 2, max = 2048, message = "Incorrect content size")
    String content;

    @JsonGetter("id")
    public int getId() {
        return id;
    }
}