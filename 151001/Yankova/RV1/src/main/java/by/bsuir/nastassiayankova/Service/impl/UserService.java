package by.bsuir.nastassiayankova.Service.impl;

import by.bsuir.nastassiayankova.Entity.User;
import by.bsuir.nastassiayankova.Service.IService;
import by.bsuir.nastassiayankova.Dto.impl.UserRequestTo;
import by.bsuir.nastassiayankova.Dto.impl.UserResponseTo;
import by.bsuir.nastassiayankova.Dto.UserMapper;
import by.bsuir.nastassiayankova.Storage.impl.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserService implements IService<UserResponseTo, UserRequestTo> {
    @Autowired
    private UserRepository userRepository;

    public List<UserResponseTo> getAll() {
        List<User> userList = userRepository.getAll();
        List<UserResponseTo> resultList = new ArrayList<>();
        for (User user : userList) {
            resultList.add(UserMapper.INSTANCE.UserToUserResponseTo(user));
        }
        return resultList;
    }

    public UserResponseTo update(UserRequestTo updatingUser) {
        User user = UserMapper.INSTANCE.UserRequestToToUser(updatingUser);
        if (validateUser(user)) {
            boolean result = userRepository.update(user);
            return result ? UserMapper.INSTANCE.UserToUserResponseTo(user) : null;
        } else return new UserResponseTo();
        //return responseTo;
    }

    public UserResponseTo get(long id) {
        return UserMapper.INSTANCE.UserToUserResponseTo(userRepository.get(id));
    }

    public UserResponseTo delete(long id) {
        return UserMapper.INSTANCE.UserToUserResponseTo(userRepository.delete(id));
    }

    public UserResponseTo add(UserRequestTo userRequestTo) {
        User user = UserMapper.INSTANCE.UserRequestToToUser(userRequestTo);
        return UserMapper.INSTANCE.UserToUserResponseTo(userRepository.insert(user));
    }

    private boolean validateUser(User user) {
        String firstname = user.getFirstname();
        ;
        String lastname = user.getLastname();
        String login = user.getLogin();
        String password = user.getPassword();
        return firstname.length() >= 2 && firstname.length() <= 64 && lastname.length() >= 2 && lastname.length() <= 64 && password.length() >= 8 && login.length() >= 2 && login.length() <= 64;
    }
}
