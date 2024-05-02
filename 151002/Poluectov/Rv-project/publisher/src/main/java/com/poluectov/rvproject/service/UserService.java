package com.poluectov.rvproject.service;

import com.poluectov.rvproject.dto.user.UserRequestTo;
import com.poluectov.rvproject.dto.user.UserResponseTo;
import com.poluectov.rvproject.model.User;
import com.poluectov.rvproject.repository.UserRepository;
import com.poluectov.rvproject.repository.jpa.JpaUserRepository;
import com.poluectov.rvproject.utils.dtoconverter.UserRequestDtoConverter;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.BiConsumer;

@Service
public class UserService extends CommonRestService<User, UserRequestTo, UserResponseTo, Long> {

    public UserService(
            UserRepository userRepository,
            UserRequestDtoConverter dtoConverter) {
        super(userRepository, dtoConverter);
    }
    @Override
    protected Optional<UserResponseTo> mapResponseTo(User user) {
        return Optional.ofNullable(UserResponseTo.builder()
                .id(user.getId())
                .login(user.getLogin())
                .hashedPassword(user.getPassword()) //TODO: hash
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .build());
    }

    @Override
    protected void update(User u1, User u2) {
        u1.setFirstName(u2.getFirstName());
        u1.setLastName(u2.getLastName());
        u1.setLogin(u2.getLogin());
        u1.setPassword(u2.getPassword());
    }
}
