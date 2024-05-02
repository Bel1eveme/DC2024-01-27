package by.bsuir.ilya.Service;

import by.bsuir.ilya.Entity.*;
import by.bsuir.ilya.Entity.User;
import by.bsuir.ilya.Entity.User;
import by.bsuir.ilya.Entity.User;
import by.bsuir.ilya.dto.*;
import by.bsuir.ilya.redis.RedisUserRepository;
import by.bsuir.ilya.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserService implements IService<UserResponseTo, UserRequestTo> {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Autowired
    private RedisUserRepository redisRepository;

    public List<UserResponseTo> getAll() {
        List<User> userList = redisRepository.getAll();;
        List<UserResponseTo> resultList = new ArrayList<>();
        if(!userList.isEmpty())
        {
            for (int i = 0; i < userList.size(); i++) {
                resultList.add(UserMapper.INSTANCE.userToUserResponseTo(userList.get(i)));
            }
        }else
        {
            userList = userRepository.findAll();
            for (int i = 0; i < userList.size(); i++) {
                resultList.add(UserMapper.INSTANCE.userToUserResponseTo(userList.get(i)));
                redisRepository.add(userList.get(i));
            }
        }
        return resultList;
    }

    public UserResponseTo update(UserRequestTo updatingUser) {
        User user = UserMapper.INSTANCE.userRequestToToUser(updatingUser);
        if (validateUser(user)) {
            UserResponseTo responseTo;
            Optional<User> redisUser = redisRepository.getById(user.getId());
            if(redisUser.isPresent() && user.equals(redisUser.get()))
            {
                return UserMapper.INSTANCE.userToUserResponseTo(redisUser.get());
            }
            try {
                User result = userRepository.save(user);
                redisRepository.update(result);
                responseTo = UserMapper.INSTANCE.userToUserResponseTo(result);
            }catch (Exception e)
            {
                e.getMessage();
                return new UserResponseTo();
            }
            return responseTo;
        } else return new UserResponseTo();
    }

    public UserResponseTo getById(long id) {
        Optional<User> redisUser = redisRepository.getById(id);
        User result;
        if(redisUser.isPresent())
        {
            return UserMapper.INSTANCE.userToUserResponseTo(redisUser.get());
        }
        else {
            result = userRepository.findById(id);
            redisRepository.add(result);
        }
        return UserMapper.INSTANCE.userToUserResponseTo(result);
    }

    public boolean deleteById(long id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            redisRepository.delete(id);
            return true;
        }return false;

    }

    public ResponseEntity<UserResponseTo> add(UserRequestTo userRequestTo) {
        User user = UserMapper.INSTANCE.userRequestToToUser(userRequestTo);
        if (validateUser(user)) {
            UserResponseTo responseTo;
            try {
                User result = userRepository.save(user);
                redisRepository.add(result);
                responseTo = UserMapper.INSTANCE.userToUserResponseTo(result);
            } catch (Exception e)
            {
                user.setId(user.getId()-1);
                return new ResponseEntity<>(UserMapper.INSTANCE.userToUserResponseTo(user), HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(responseTo, HttpStatus.CREATED);
        } else return new ResponseEntity<>(UserMapper.INSTANCE.userToUserResponseTo(user), HttpStatus.FORBIDDEN);
    }

    private boolean validateUser(User user) {
        if(user.getFirstname()!=null && user.getLastname()!=null && user.getLogin()!=null && user.getPassword()!=null) {
            String firstname = user.getFirstname();
            String lastname = user.getLastname();
            String login = user.getLogin();
            String password = user.getPassword();
            if ((firstname.length() >= 2 && firstname.length() <= 64) &&
                    (lastname.length() >= 2 && lastname.length() <= 64) &&
                    (password.length() >= 8 && firstname.length() <= 128) &&
                    (login.length() >= 2 && login.length() <= 64)) return true;
        }
        return false;
    }
}
