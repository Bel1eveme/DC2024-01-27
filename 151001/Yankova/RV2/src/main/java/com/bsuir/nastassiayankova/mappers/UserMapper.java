package com.bsuir.nastassiayankova.mappers;

import com.bsuir.nastassiayankova.beans.entity.User;
import com.bsuir.nastassiayankova.beans.response.UserResponseTo;
import org.mapstruct.Mapper;
import com.bsuir.nastassiayankova.beans.request.UserRequestTo;

@Mapper(componentModel = "spring", uses = NewsListMapper.class)
public interface UserMapper {
    User toUser(UserRequestTo userRequestTo);

    UserResponseTo toUserResponseTo(User user);
}
