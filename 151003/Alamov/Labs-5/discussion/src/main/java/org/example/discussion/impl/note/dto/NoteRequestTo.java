package org.example.discussion.impl.note.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteRequestTo {
    private BigInteger id;
    private BigInteger tweetId;
    @Size(min = 2, max = 2048)
    String content;
}
