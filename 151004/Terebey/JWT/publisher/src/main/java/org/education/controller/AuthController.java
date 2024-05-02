package org.education.controller;

import org.education.bean.dto.CreatorResponseTo;
import org.education.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<CreatorResponseTo> signUp(@RequestBody CreatorResponseTo signUpRequest){
        CreatorResponseTo res = authService.signUp(signUpRequest);
        System.out.println(ResponseEntity.ok(res));
        return (ResponseEntity<CreatorResponseTo>) ResponseEntity.ok(res);
    }
    @PostMapping("/signin")
    public ResponseEntity<CreatorResponseTo> signIn(@RequestBody CreatorResponseTo signInRequest){
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }
    @PostMapping("/refresh")
    public ResponseEntity<CreatorResponseTo> refreshToken(@RequestBody CreatorResponseTo refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
}
