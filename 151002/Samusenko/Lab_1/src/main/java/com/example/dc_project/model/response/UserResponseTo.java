package com.example.dc_project.model.response;

public record UserResponseTo(
        Long id,
        String login,
        String password,
        String firstname,
        String lastname
) {
}
