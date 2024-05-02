package by.bsuir.mapper;

import by.bsuir.dto.AuthorRequestTo;
import by.bsuir.dto.AuthorResponseTo;
import by.bsuir.entities.Author;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = AuthorMapper.class)
public interface AuthorListMapper {
    List<Author> toAuthorList(List<AuthorRequestTo> authors);
    List<AuthorResponseTo> toAuthorResponseList(List<Author> authors);
}
