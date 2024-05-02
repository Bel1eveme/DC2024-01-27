package com.bsuir.nastassiayankova.mappers;

import com.bsuir.nastassiayankova.beans.entity.User;
import com.bsuir.nastassiayankova.beans.request.UserRequestTo;
import com.bsuir.nastassiayankova.beans.response.UserResponseTo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserListMapper {
    List<User> toUserList(List<UserRequestTo> userRequestToList);

    List<UserResponseTo> toUserResponseToList(List<User> userList);
}
