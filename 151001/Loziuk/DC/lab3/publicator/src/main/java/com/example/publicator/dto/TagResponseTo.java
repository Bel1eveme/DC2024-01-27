package com.example.publicator.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TagResponseTo {
    int id;
    @Size(min = 2, max = 32)
    String name;
    int issueId;
}
