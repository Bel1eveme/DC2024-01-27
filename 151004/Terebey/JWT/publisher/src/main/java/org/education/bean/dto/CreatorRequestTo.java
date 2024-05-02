package org.education.bean.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatorRequestTo {
    int id;

    @Size(min = 2, max = 64, message = "Incorrect login size")
    String login;
    @Size(min = 8, max = 128, message = "Incorrect password size")
    String password;
    @Size(min = 2, max = 64, message = "Incorrect firstname size")
    String firstname;
    @Size(min = 2, max = 64, message = "Incorrect lastname size")
    String lastname;
    @Size(min = 2, max = 64, message = "Incorrect role size")
    String role;
}