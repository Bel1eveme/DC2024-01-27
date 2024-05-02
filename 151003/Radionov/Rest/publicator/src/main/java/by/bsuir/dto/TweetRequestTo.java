package by.bsuir.dto;

import by.bsuir.utils.StringDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetRequestTo {
    private Long id;
    @NotBlank
    @Size(min = 2, max = 64)
    private String title;
    @JsonDeserialize(using = StringDeserializer.class)
    private String content;
    private Long authorId;
    private Long stickerId;
}
