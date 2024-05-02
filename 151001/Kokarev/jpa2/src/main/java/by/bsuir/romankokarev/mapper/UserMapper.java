package by.bsuir.romankokarev.mapper;

import by.bsuir.romankokarev.dto.UserRequestTo;
import by.bsuir.romankokarev.dto.UserResponseTo;
import by.bsuir.romankokarev.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userRequestToUser(UserRequestTo userRequestTo);

    UserResponseTo userToUserResponse(User user);
}
