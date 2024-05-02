package com.bsuir.nastassiayankova.controllers;

import com.bsuir.nastassiayankova.beans.request.UserRequestTo;
import com.bsuir.nastassiayankova.beans.response.UserResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bsuir.nastassiayankova.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseTo> createUser(@RequestBody UserRequestTo userRequestTo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userRequestTo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseTo> findUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseTo>> findAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.read());
    }

    @PutMapping
    public ResponseEntity<UserResponseTo> updateUser(@RequestBody UserRequestTo userRequestTo) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(userRequestTo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteUserById(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(id);
    }
}
