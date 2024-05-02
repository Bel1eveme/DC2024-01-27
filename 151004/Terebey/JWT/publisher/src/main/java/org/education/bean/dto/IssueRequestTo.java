package org.education.bean.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IssueRequestTo {
    int id;
    @Min(value = 0, message = "creatorId must be >= 0")
    int creatorId;

    @Size(min = 2, max = 64, message = "Incorrect title size")
    String title;

    @Size(min = 4, max = 2048, message = "Incorrect content size")
    String content;
}