package by.bsuir.nastassiayankova.Dto;

import by.bsuir.nastassiayankova.Entity.User;
import by.bsuir.nastassiayankova.Dto.impl.UserRequestTo;
import by.bsuir.nastassiayankova.Dto.impl.UserResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponseTo UserToUserResponseTo(User user);

    UserRequestTo UserToUserRequestTo(User user);

    User UserResponseToToUser(UserResponseTo userResponseTo);

    User UserRequestToToUser(UserRequestTo userRequestTo);
}
