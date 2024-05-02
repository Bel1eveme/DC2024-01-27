package com.example.rv.impl.note.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class NoteRequestTo {
    private BigInteger id;
    private BigInteger tweetId;
    @Size(min = 2, max = 2048)
    String content;
}
