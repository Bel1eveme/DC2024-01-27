package org.education.bean.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.education.bean.Creator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatorResponseTo {
    private String login;
    private String password;
    private String firstname;
    private String lastname;
    private String role;
    private String token;
    private Creator creator;
    private int statusCode;
    private String error;
    private String message;
    private String refreshToken;
    private String expirationTime;
}