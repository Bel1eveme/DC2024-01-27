package by.bsuir.romankokarev.mapper;

import by.bsuir.romankokarev.dto.UserRequestTo;
import by.bsuir.romankokarev.dto.UserResponseTo;
import by.bsuir.romankokarev.model.User;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserListMapper {
    List<User> toUserList(List<UserRequestTo> users);

    List<UserResponseTo> toUserResponseList(List<User> users);
}
