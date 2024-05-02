package com.example.discussion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteKey implements Serializable {
    private String country;
    private int issueId;
    private int id;
}
