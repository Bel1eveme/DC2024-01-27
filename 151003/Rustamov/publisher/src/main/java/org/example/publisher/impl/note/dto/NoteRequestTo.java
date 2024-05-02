package org.example.publisher.impl.note.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class NoteRequestTo {
    private BigInteger id;
    private BigInteger newsId;
    @Size(min = 2, max = 2048)
    String content;
}
