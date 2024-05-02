package com.example.restapplication.services.impl;

import com.example.restapplication.exceptions.DuplicationException;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.PageRequest;
import com.example.restapplication.dto.UserRequestTo;
import com.example.restapplication.dto.UserResponseTo;
import com.example.restapplication.entites.User;
import com.example.restapplication.exceptions.DeleteException;
import com.example.restapplication.exceptions.NotFoundException;
import com.example.restapplication.exceptions.UpdateException;
import com.example.restapplication.mappers.UserListMapper;
import com.example.restapplication.mappers.UserMapper;
import com.example.restapplication.repository.UserRepository;
import com.example.restapplication.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
@CacheConfig(cacheNames = "usersCache")
public class ImplUserService implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepository userDAO;

    @Autowired
    UserListMapper userListMapper;
    @Override
    @Cacheable(cacheNames = "users", key = "#id", unless = "#result == null")
    public UserResponseTo getById(Long id) throws NotFoundException {
        Optional<User> user = userDAO.findById(id);
        return user.map(value -> userMapper.toUserResponse(value)).orElseThrow(() -> new NotFoundException("User not found", 40004L));
    }

    @Override
    @Cacheable(cacheNames = "users")
    public List<UserResponseTo> getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable;
        if (sortOrder != null && sortOrder.equals("asc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        }
        Page<User> users = userDAO.findAll(pageable);
        return userListMapper.toUserResponseList(users.toList());
    }


    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public UserResponseTo save(@Valid UserRequestTo requestTo) {
        User userToSave = userMapper.toUser(requestTo);
        if (userDAO.existsByLogin(requestTo.getLogin())) {
            throw new DuplicationException("Login duplication", 40005L);
        }
        return userMapper.toUserResponse(userDAO.save(userToSave));
    }

    @Override
    @Caching(evict = { @CacheEvict(cacheNames = "users", key = "#id"),
            @CacheEvict(cacheNames = "users", allEntries = true) })
    public void delete(Long id) throws DeleteException {
        if (!userDAO.existsById(id)) {
            throw new DeleteException("User not found!", 40004L);
        } else {
            userDAO.deleteById(id);
        }
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public UserResponseTo update(@Valid UserRequestTo requestTo) throws UpdateException {
        User userToUpdate = userMapper.toUser(requestTo);
        if (!userDAO.existsById(userToUpdate.getId())) {
            throw new UpdateException("User not found!", 40004L);
        } else {
            return userMapper.toUserResponse(userDAO.save(userToUpdate));
        }
    }

    @Override
    public UserResponseTo getByStoryId(Long storyId) throws NotFoundException {
        User user = userDAO.findUserByStory(storyId);
        return userMapper.toUserResponse(user);
    }
}
