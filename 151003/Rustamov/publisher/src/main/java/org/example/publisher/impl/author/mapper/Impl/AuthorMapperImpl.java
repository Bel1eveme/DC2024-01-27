package org.example.publisher.impl.author.mapper.Impl;

import org.example.publisher.impl.author.Author;
import org.example.publisher.impl.author.dto.AuthorRequestTo;
import org.example.publisher.impl.author.dto.AuthorResponseTo;
import org.example.publisher.impl.author.mapper.AuthorMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorMapperImpl implements AuthorMapper {
    @Override
    public AuthorRequestTo authorToRequestTo(Author author) {
        return new AuthorRequestTo(author.getId(),
                author.getLogin(),
                author.getPassword(),
                author.getFirstname(),
                author.getLastname());
    }

    @Override
    public List<AuthorRequestTo> authorToRequestTo(Iterable<Author> authors) {
        return StreamSupport.stream(authors.spliterator(), false)
                .map(this::authorToRequestTo)
                .collect(Collectors.toList());
    }

    @Override
    public Author dtoToEntity(AuthorRequestTo authorRequestTo) {
        return new Author(authorRequestTo.getId(),
                authorRequestTo.getLogin(),
                authorRequestTo.getPassword(),
                authorRequestTo.getFirstname(),
                authorRequestTo.getLastname());
    }

    @Override
    public List<Author> dtoToEntity(Iterable<AuthorRequestTo> authorRequestTos) {
        return StreamSupport.stream(authorRequestTos.spliterator(), false)
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorResponseTo authorToResponseTo(Author author) {
        return new AuthorResponseTo(author.getId(),
                author.getLogin(),
                author.getFirstname(),
                author.getLastname());
    }

    @Override
    public List<AuthorResponseTo> authorToResponseTo(Iterable<Author> authors) {
        return StreamSupport.stream(authors.spliterator(), false)
                .map(this::authorToResponseTo)
                .collect(Collectors.toList());
    }
}
