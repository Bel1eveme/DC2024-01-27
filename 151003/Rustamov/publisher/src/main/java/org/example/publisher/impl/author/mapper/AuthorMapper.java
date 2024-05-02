package org.example.publisher.impl.author.mapper;

import org.example.publisher.impl.author.Author;
import org.example.publisher.impl.author.dto.AuthorRequestTo;
import org.example.publisher.impl.author.dto.AuthorResponseTo;

import java.util.List;

public interface AuthorMapper {

    AuthorRequestTo authorToRequestTo(Author author);

    List<AuthorRequestTo> authorToRequestTo(Iterable<Author> authors);

    Author dtoToEntity(AuthorRequestTo authorRequestTo);

    List<Author> dtoToEntity(Iterable<AuthorRequestTo> authorRequestTos);

    AuthorResponseTo authorToResponseTo(Author author);

    List<AuthorResponseTo> authorToResponseTo(Iterable<Author> authors);
    
    
}
