package com.example.rv.impl.editor.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class EditorRequestTo {
    private BigInteger id;

    @Size(min = 2, max = 64)
    private String login;

    @Size(min = 8, max = 128)
    private String password;

    @Size(min = 2, max = 64)
    private String firstname;

    @Size(min = 2, max = 64)
    private String lastname;
}
