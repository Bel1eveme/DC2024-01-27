package org.education.bean.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LabelRequestTo {
    int id;

    @Size(min = 2, max = 32, message = "Incorrect name size")
    String name;
}