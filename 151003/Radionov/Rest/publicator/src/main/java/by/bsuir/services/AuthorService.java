package by.bsuir.services;

import by.bsuir.dto.AuthorRequestTo;
import by.bsuir.dto.AuthorResponseTo;
import by.bsuir.entities.Author;
import by.bsuir.exceptions.DeleteException;
import by.bsuir.exceptions.DuplicationException;
import by.bsuir.exceptions.NotFoundException;
import org.springframework.cache.annotation.*;
import by.bsuir.exceptions.UpdateException;
import by.bsuir.mapper.AuthorListMapper;
import by.bsuir.mapper.AuthorMapper;
import by.bsuir.repository.AuthorRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
@CacheConfig(cacheNames = "editorsCache")
public class AuthorService {
    @Autowired
    AuthorMapper authorMapper;
    @Autowired
    AuthorRepository authorDao;
    @Autowired
    AuthorListMapper authorListMapper;

    @Cacheable(cacheNames = "editors", key = "#id", unless = "#result == null")
    public AuthorResponseTo getAuthorById(@Min(0) Long id) throws NotFoundException {
        Optional<Author> author = authorDao.findById(id);
        return author.map(value -> authorMapper.authorToAuthorResponse(value)).orElseThrow(() -> new NotFoundException("Author not found!", 40004L));
    }

    @Cacheable(cacheNames = "editors")
    public List<AuthorResponseTo> getAuthors(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable;
        if (sortOrder != null && sortOrder.equals("asc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        }
        Page<Author> authors = authorDao.findAll(pageable);
        return authorListMapper.toAuthorResponseList(authors.toList());
    }

    @CacheEvict(cacheNames = "editors", allEntries = true)
    public AuthorResponseTo saveAuthor(@Valid AuthorRequestTo author) throws DuplicationException {
        Author authorToSave = authorMapper.authorRequestToAuthor(author);
        if (authorDao.existsByLogin(authorToSave.getLogin())) {
            throw new DuplicationException("Login duplication", 40005L);
        }
        return authorMapper.authorToAuthorResponse(authorDao.save(authorToSave));
    }

    @Caching(evict = { @CacheEvict(cacheNames = "editors", key = "#id"),
            @CacheEvict(cacheNames = "editors", allEntries = true) })
    public void deleteAuthor(@Min(0) Long id) throws DeleteException {
        if (!authorDao.existsById(id)) {
            throw new DeleteException("Author not found!", 40004L);
        } else {
            authorDao.deleteById(id);
        }
    }

    @CacheEvict(cacheNames = "editors", allEntries = true)
    public AuthorResponseTo updateAuthor(@Valid AuthorRequestTo author) throws UpdateException {
        Author authorToUpdate = authorMapper.authorRequestToAuthor(author);
        if (!authorDao.existsById(authorToUpdate.getId())) {
            throw new UpdateException("Author not found!", 40004L);
        } else {
            return authorMapper.authorToAuthorResponse(authorDao.save(authorToUpdate));
        }
    }

    public AuthorResponseTo getAuthorByTweetId(@Min(0) Long tweetId) throws NotFoundException {
        Author author = authorDao.findAuthorByTweetId(tweetId);
        return authorMapper.authorToAuthorResponse(author);
    }
}
