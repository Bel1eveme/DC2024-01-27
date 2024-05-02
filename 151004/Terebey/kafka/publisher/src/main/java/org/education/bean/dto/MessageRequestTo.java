package org.education.bean.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageRequestTo {
    int id;

    @Min(value = 0)
    int issueId;

    @Size(min = 2, max = 2048, message = "Incorrect content size")
    String content;
}