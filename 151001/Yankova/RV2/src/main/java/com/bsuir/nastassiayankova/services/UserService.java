package com.bsuir.nastassiayankova.services;

import com.bsuir.nastassiayankova.beans.entity.User;
import com.bsuir.nastassiayankova.beans.entity.ValidationMarker;
import com.bsuir.nastassiayankova.beans.request.UserRequestTo;
import com.bsuir.nastassiayankova.beans.response.UserResponseTo;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

public interface UserService {
    @Validated(ValidationMarker.OnCreate.class)
    UserResponseTo create(@Valid UserRequestTo entity);

    List<UserResponseTo> read();

    @Validated(ValidationMarker.OnUpdate.class)
    UserResponseTo update(@Valid UserRequestTo entity);

    void delete(Long id);

    UserResponseTo findUserById(Long id);

    Optional<User> findUserByIdExt(Long id);
}
