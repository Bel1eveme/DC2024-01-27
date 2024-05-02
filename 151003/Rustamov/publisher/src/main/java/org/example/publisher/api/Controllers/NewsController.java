package org.example.publisher.api.Controllers;

import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.impl.news.*;
import org.example.publisher.impl.news.dto.NewsRequestTo;
import org.example.publisher.impl.news.dto.NewsResponseTo;
import org.example.publisher.impl.news.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NewsResponseTo> getNews() {
        return newsService.getNews();
    }

    @GetMapping("/{id}")
    public NewsResponseTo getNewsById(@PathVariable BigInteger id) throws EntityNotFoundException {
        return newsService.getNewsById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewsResponseTo createNews(@Valid @RequestBody NewsRequestTo newsRequestTo) throws EntityNotFoundException, DuplicateEntityException {
        return newsService.saveNews(newsRequestTo);
    }


    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public NewsResponseTo updateIssue(@Valid @RequestBody NewsRequestTo news) throws EntityNotFoundException, DuplicateEntityException {
        return newsService.updateIssue(news);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIssue(@PathVariable BigInteger id) throws EntityNotFoundException {
        newsService.deleteIssue(id);
    }
}
