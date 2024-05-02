package org.example.publisher.impl.author.service;

import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.impl.author.Author;
import org.example.publisher.impl.author.AuthorRepository;
import org.example.publisher.impl.author.dto.AuthorRequestTo;
import org.example.publisher.impl.author.dto.AuthorResponseTo;
import org.example.publisher.impl.author.mapper.Impl.AuthorMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "authorsCache")
public class AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapperImpl authorMapper;
    private final String ENTITY_NAME = "author";


    @Cacheable(cacheNames = "authors")
    public List<AuthorResponseTo> getAuthors(){
        List<Author> authors = authorRepository.findAll();
        return authorMapper.authorToResponseTo(authors);
    }
    @Cacheable(cacheNames = "authors", key = "#id", unless = "#result == null")
    public AuthorResponseTo getAuthorById(BigInteger id) throws EntityNotFoundException{
        Optional<Author> author = authorRepository.findById(id);
        if (author.isEmpty()){
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        return authorMapper.authorToResponseTo(author.get());
    }

    @CacheEvict(cacheNames = "authors", allEntries = true)
    public AuthorResponseTo createAuthor(AuthorRequestTo author) throws DuplicateEntityException {
        try {
            Author savedAuthor = authorRepository.save(authorMapper.dtoToEntity(author));
            return authorMapper.authorToResponseTo(savedAuthor);

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException(ENTITY_NAME, "login");
        }
    }

    @CacheEvict(cacheNames = "authors", allEntries = true)
    public AuthorResponseTo updateAuthor(AuthorRequestTo author) throws EntityNotFoundException {
        if (authorRepository.findById(author.getId()).isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, author.getId());
        }
        Author savedAuthor = authorRepository.save(authorMapper.dtoToEntity(author));
        return authorMapper.authorToResponseTo(savedAuthor);
    }

    @Caching(evict = { @CacheEvict(cacheNames = "authors", key = "#id"),
            @CacheEvict(cacheNames = "authors", allEntries = true) })
    public void deleteAuthor(BigInteger id) throws EntityNotFoundException {
        if (authorRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        authorRepository.deleteById(id);
    }
}
