package com.example.discussion.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NoteResponseTo {
    String country;
    int id;
    int issueId;
    @Size(min = 2, max = 2048)
    String content;
}
