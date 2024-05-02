package by.bsuir.mapper;

import by.bsuir.dto.AuthorRequestTo;
import by.bsuir.dto.AuthorResponseTo;
import by.bsuir.entities.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author authorRequestToAuthor(AuthorRequestTo authorRequestTo);

    AuthorResponseTo authorToAuthorResponse(Author author);
}

