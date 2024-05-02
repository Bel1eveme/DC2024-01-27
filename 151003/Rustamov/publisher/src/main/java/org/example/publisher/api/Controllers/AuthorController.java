package org.example.publisher.api.Controllers;

import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.impl.author.dto.AuthorRequestTo;
import org.example.publisher.impl.author.dto.AuthorResponseTo;
import org.example.publisher.impl.author.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<AuthorResponseTo> getAuthors() {
        return authorService.getAuthors();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    AuthorResponseTo getAuthorById(@PathVariable BigInteger id) throws EntityNotFoundException {
        return authorService.getAuthorById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    AuthorResponseTo makeAuthor(@Valid @RequestBody AuthorRequestTo authorRequestTo) throws DuplicateEntityException {
        return authorService.createAuthor(authorRequestTo);
    }

    @PutMapping
    AuthorResponseTo updateAuthor(@Valid @RequestBody AuthorRequestTo authorRequestTo) throws EntityNotFoundException{
        return authorService.updateAuthor(authorRequestTo);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    void deleteAuthor(@PathVariable BigInteger id) throws EntityNotFoundException {
        authorService.deleteAuthor(id);
    }
}
