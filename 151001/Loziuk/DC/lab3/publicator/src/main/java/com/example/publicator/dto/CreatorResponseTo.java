package com.example.publicator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatorResponseTo {
    int id;
    String login;
    String password;
    String firstname;
    String lastname;
}
