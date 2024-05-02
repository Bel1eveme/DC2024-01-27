package com.example.dc_project.controller;

import com.example.dc_project.model.request.UserRequestTo;
import com.example.dc_project.model.response.UserResponseTo;
import com.example.dc_project.service.UserService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/users")
@Data
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseTo findById(@Valid @PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseTo> findAll() {
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseTo create(@Valid @RequestBody UserRequestTo request) {
        return userService.create(request);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponseTo update(@Valid @RequestBody UserRequestTo request) {
        return userService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean removeById(@Valid @PathVariable("id") Long id) {
        return userService.removeById(id);
    }
}
