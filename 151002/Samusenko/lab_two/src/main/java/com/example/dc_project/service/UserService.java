package com.example.dc_project.service;

import com.example.dc_project.dao.repository.UserRepository;
import com.example.dc_project.model.entity.User;
import com.example.dc_project.model.request.UserRequestTo;
import com.example.dc_project.model.response.UserResponseTo;
import com.example.dc_project.service.exceptions.DuplicateException;
import com.example.dc_project.service.exceptions.ResourceNotFoundException;
import com.example.dc_project.service.exceptions.ResourceStateException;
import com.example.dc_project.service.mapper.UserMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class UserService implements IService<UserRequestTo, UserResponseTo>{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public UserResponseTo findById(Long id) {
        return userRepository.getById(id).map(userMapper::getResponse).orElseThrow(() -> findByIdException(id));
    }
    @Override
    public List<UserResponseTo> findAll() {
        return userMapper.getListResponse(userRepository.getAll());
    }
    @Override
    public UserResponseTo create(UserRequestTo request) {
        for (User curr_user: userRepository.getAll()) {
            if(request.login().equals(curr_user.getLogin()))
                throw duplicateException(request.login());
        }
            return userRepository.save(userMapper.getUser(request)).map(userMapper::getResponse).orElseThrow(UserService::createException);
    }
    @Override
    public UserResponseTo update(UserRequestTo request) {
        if (userMapper.getUser(request).getId() == null)
        {
            throw findByIdException(userMapper.getUser(request).getId());
        }

        return userRepository.update(userMapper.getUser(request)).map(userMapper::getResponse).orElseThrow(UserService::updateException);
    }
    @Override
    public boolean removeById(Long id) {
        if (!userRepository.removeById(id)) {
            throw removeException();
        }
        return true;
    }
    private static ResourceNotFoundException findByIdException(Long id) {
        return new ResourceNotFoundException(HttpStatus.NOT_FOUND.value() * 100 + 11, "Can't find user by id = " + id);
    }
    private static ResourceStateException createException() {
        return new ResourceStateException(HttpStatus.CONFLICT.value() * 100 + 12, "Can't create user");
    }
    private static ResourceStateException updateException() {
        return new ResourceStateException(HttpStatus.CONFLICT.value() * 100 + 13, "Can't update user");
    }
    private static ResourceStateException removeException() {
        return new ResourceStateException(HttpStatus.CONFLICT.value() * 100 + 14, "Can't remove user");
    }

    private static DuplicateException duplicateException(String login){

        return new DuplicateException(403, "User with login '"+login+"' already exists");
    }
}
