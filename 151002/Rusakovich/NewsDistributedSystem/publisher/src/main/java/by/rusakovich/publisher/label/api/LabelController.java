package by.rusakovich.publisher.label.api;

import by.rusakovich.publisher.generics.api.Controller;
import by.rusakovich.publisher.label.LabelService;
import by.rusakovich.publisher.label.model.LabelRequestTO;
import by.rusakovich.publisher.label.model.LabelResponseTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.url}/labels")
public class LabelController extends Controller<Long, LabelRequestTO, LabelResponseTO, LabelService> {

    public LabelController(LabelService service) {
        super(service);
    }
}
