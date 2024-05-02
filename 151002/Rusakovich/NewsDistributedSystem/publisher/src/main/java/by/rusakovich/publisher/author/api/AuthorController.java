package by.rusakovich.publisher.author.api;

import by.rusakovich.publisher.author.AuthorService;
import by.rusakovich.publisher.author.model.AuthorRequestTO;
import by.rusakovich.publisher.author.model.AuthorResponseTO;
import by.rusakovich.publisher.generics.api.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.url}/authors")
public class AuthorController extends Controller<Long, AuthorRequestTO, AuthorResponseTO, AuthorService> {
    public AuthorController(AuthorService service) {
        super(service);
    }
}
