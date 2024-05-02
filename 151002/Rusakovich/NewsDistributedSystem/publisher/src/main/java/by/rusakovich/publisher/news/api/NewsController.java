package by.rusakovich.publisher.news.api;

import by.rusakovich.publisher.generics.api.Controller;
import by.rusakovich.publisher.news.NewsService;
import by.rusakovich.publisher.news.model.NewsRequestTO;
import by.rusakovich.publisher.news.model.NewsResponseTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.url}/news")
public class NewsController extends Controller<Long, NewsRequestTO, NewsResponseTO, NewsService> {

    public NewsController(NewsService service) {
        super(service);
    }
}
