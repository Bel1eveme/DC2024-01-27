package com.bsuir.nastassiayankova.services.impl;

import com.bsuir.nastassiayankova.beans.entity.User;
import com.bsuir.nastassiayankova.beans.entity.ValidationMarker;
import com.bsuir.nastassiayankova.mappers.UserListMapper;
import com.bsuir.nastassiayankova.mappers.UserMapper;
import com.bsuir.nastassiayankova.beans.request.UserRequestTo;
import com.bsuir.nastassiayankova.beans.response.UserResponseTo;
import com.bsuir.nastassiayankova.exceptions.NoSuchUserException;
import com.bsuir.nastassiayankova.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.bsuir.nastassiayankova.services.UserService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Validated
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserListMapper userListMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserListMapper userListMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userListMapper = userListMapper;
    }

    @Override
    @Validated(ValidationMarker.OnCreate.class)
    public UserResponseTo create(@Valid UserRequestTo entity) {
        return userMapper.toUserResponseTo(userRepository.save(userMapper.toUser(entity)));
    }

    @Override
    public List<UserResponseTo> read() {
        return userListMapper.toUserResponseToList(userRepository.findAll());
    }

    // Можно сразу сделать проверку != и выкинуть исключение, но так более читабельно :)
    @Override
    @Validated(ValidationMarker.OnUpdate.class)
    public UserResponseTo update(@Valid UserRequestTo entity) {
        if (userRepository.existsById(entity.id())) {
            User user = userMapper.toUser(entity);
            user.setNewsList(userRepository.getReferenceById(user.getId()).getNewsList());
            return userMapper.toUserResponseTo(userRepository.save(user));
        } else {
            throw new NoSuchUserException(entity.id());
        }

    }

    @Override
    public void delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new NoSuchUserException(id);
        }
    }

    @Override
    public UserResponseTo findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchUserException(id));
        return userMapper.toUserResponseTo(user);
    }

    @Override
    public Optional<User> findUserByIdExt(Long id) {
        return userRepository.findById(id);
    }
}
