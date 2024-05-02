package by.bsuir.controllers;

import by.bsuir.dto.AuthorRequestTo;
import by.bsuir.dto.AuthorResponseTo;
import by.bsuir.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/authors")
public class AuthorController {
    @Autowired
    AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorResponseTo>> getAuthors(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        return ResponseEntity.status(200).body(authorService.getAuthors(pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseTo> getAuthor(@PathVariable Long id) {
        return ResponseEntity.status(200).body(authorService.getAuthorById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<AuthorResponseTo> saveAuthor(@RequestBody AuthorRequestTo author) {
        AuthorResponseTo savedAuthor = authorService.saveAuthor(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
    }

    @PutMapping
    public ResponseEntity<AuthorResponseTo> updateAuthor(@RequestBody AuthorRequestTo author) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.updateAuthor(author));
    }

    @GetMapping("/byTweet/{id}")
    public ResponseEntity<AuthorResponseTo> getAuthorByTweetId(@PathVariable Long id){
        return ResponseEntity.status(200).body(authorService.getAuthorByTweetId(id));
    }
}
