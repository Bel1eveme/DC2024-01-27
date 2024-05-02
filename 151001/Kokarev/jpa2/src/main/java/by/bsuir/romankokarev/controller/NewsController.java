package by.bsuir.romankokarev.controller;

import by.bsuir.romankokarev.service.NewsService;
import by.bsuir.romankokarev.dto.NewsRequestTo;
import by.bsuir.romankokarev.dto.NewsResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/")
public class NewsController {
    @Autowired
    NewsService newsService;

    @GetMapping(value = "news", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> readAll(
            @RequestParam(required = false, defaultValue = "0") Integer pageInd,
            @RequestParam(required = false, defaultValue = "20") Integer numOfElem,
            @RequestParam(required = false, defaultValue = "id") String sortedBy,
            @RequestParam(required = false, defaultValue = "desc") String direction) {
        List<NewsResponseTo> list = newsService.readAll(pageInd, numOfElem, sortedBy, direction);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "news/{id}")
    public ResponseEntity<?> read(@PathVariable(name = "id") int id) {
        NewsResponseTo news = newsService.read(id);

        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @PostMapping(value = "news")
    public ResponseEntity<?> create(@RequestBody NewsRequestTo newsRequestTo) {
        NewsResponseTo newsResponseTo = newsService.create(newsRequestTo);

        return new ResponseEntity<>(newsResponseTo, HttpStatus.CREATED);
    }

    @PutMapping(value = "news")
    public ResponseEntity<?> update(@RequestBody NewsRequestTo newsRequestTo) {
        NewsResponseTo newsResponseTo = newsService.update(newsRequestTo, newsRequestTo.getId());

        return new ResponseEntity<>(newsResponseTo, HttpStatus.OK);
    }

    @DeleteMapping(value = "news/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        newsService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
