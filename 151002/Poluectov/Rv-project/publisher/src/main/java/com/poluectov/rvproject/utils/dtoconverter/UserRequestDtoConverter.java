package com.poluectov.rvproject.utils.dtoconverter;

import com.poluectov.rvproject.dto.user.UserRequestTo;
import com.poluectov.rvproject.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserRequestDtoConverter implements DtoConverter<UserRequestTo, User> {

    @Override
    public User convert(UserRequestTo userRequestTo) {
        return User.builder()
                .id(userRequestTo.getId())
                .login(userRequestTo.getLogin())
                .password(userRequestTo.getPassword()) // TODO: Hash password
                .firstName(userRequestTo.getFirstname())
                .lastName(userRequestTo.getLastname())
                .build();
    }
}
