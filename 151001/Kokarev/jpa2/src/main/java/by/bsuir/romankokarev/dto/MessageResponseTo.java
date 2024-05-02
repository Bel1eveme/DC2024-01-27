package by.bsuir.romankokarev.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MessageResponseTo {
    int id;
    int newsId;
    @Size(min = 2, max = 2048)
    String content;
}
