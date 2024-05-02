package com.poluectov.rvproject.utils.dtoconverter;

import com.poluectov.rvproject.dto.user.UserResponseTo;
import com.poluectov.rvproject.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserResponseDtoConverter implements DtoConverter<User, UserResponseTo> {

    @Override
    public UserResponseTo convert(User user) {
        return UserResponseTo.builder()
                .id(user.getId())
                .login(user.getLogin())
                .hashedPassword(user.getPassword())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .build();
    }
}
