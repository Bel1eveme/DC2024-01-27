package by.bsuir.romankokarev.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsRequestTo {
    private int id;
    private int userId;
    @Size(min = 2, max = 64)
    private String title;
    @Size(min = 4, max = 2048)
    private String content;
    private Timestamp created;
    private Timestamp modified;
    private int tagId;
}
