package by.bsuir.romankokarev.service;

import by.bsuir.romankokarev.exception.DuplicateException;
import by.bsuir.romankokarev.dto.NewsRequestTo;
import by.bsuir.romankokarev.dto.NewsResponseTo;
import by.bsuir.romankokarev.exception.NotFoundException;
import by.bsuir.romankokarev.mapper.NewsListMapper;
import by.bsuir.romankokarev.mapper.NewsMapper;
import by.bsuir.romankokarev.model.News;
import by.bsuir.romankokarev.repository.UserRepository;
import by.bsuir.romankokarev.repository.NewsRepository;
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

@Service
@Validated
public class NewsService {
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    NewsListMapper newsListMapper;
    @Autowired
    NewsRepository newsRepository;
    @Autowired
    UserRepository userRepository;

    public NewsResponseTo create(@Valid NewsRequestTo newsRequestTo) {
        News news = newsMapper.newsRequestToNews(newsRequestTo);
        if (newsRepository.existsByTitle(news.getTitle())) {
            throw new DuplicateException("Title duplication", 403);
        }
        if (newsRequestTo.getUserId() != 0) {
            news.setUser(userRepository.findById(newsRequestTo.getUserId()).orElseThrow(() -> new NotFoundException("User not found", 404)));
        }

        return newsMapper.newsToNewsResponse(newsRepository.save(news));
    }

    public NewsResponseTo read(@Min(0) int id) throws NotFoundException {
        if (newsRepository.existsById(id)) {
            NewsResponseTo news = newsMapper.newsToNewsResponse(newsRepository.getReferenceById(id));

            return news;
        }

        throw new NotFoundException("News not found", 404);
    }

    public List<NewsResponseTo> readAll(int pageInd, int numOfElem, String sortedBy, String direction) {
        Pageable p;
        if (direction != null && direction.equals("asc")) {
            p = PageRequest.of(pageInd, numOfElem, Sort.by(sortedBy).ascending());
        } else {
            p = PageRequest.of(pageInd, numOfElem, Sort.by(sortedBy).descending());
        }
        Page<News> res = newsRepository.findAll(p);

        return newsListMapper.toNewsResponseList(res.toList());
    }

    public NewsResponseTo update(@Valid NewsRequestTo newsRequestTo, @Min(0) int id) throws NotFoundException {
        if (newsRepository.existsById(id)) {
            News news = newsMapper.newsRequestToNews(newsRequestTo);
            news.setId(id);
            if (newsRequestTo.getUserId() != 0) {
                news.setUser(userRepository.findById(newsRequestTo.getUserId()).orElseThrow(() -> new NotFoundException("User not found", 404)));
            }

            return newsMapper.newsToNewsResponse(newsRepository.save(news));
        }

        throw new NotFoundException("News not found", 404);
    }

    public boolean delete(@Min(0) int id) throws NotFoundException {
        if (newsRepository.existsById(id)) {
            newsRepository.deleteById(id);

            return true;
        }

        throw new NotFoundException("News not found", 404);
    }
}
