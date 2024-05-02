package dtalalaev.rv.impl.model.story;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/storys")
public class StoryController {

    @Autowired
    private StoryService service;


    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Story> findAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StoryResponseTo findOne(@PathVariable("id") BigInteger id) throws ResponseStatusException {
        return service.findOne(id);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoryResponseTo create(@Valid @RequestBody StoryRequestTo dto) throws ResponseStatusException{
        return service.create(dto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public StoryResponseTo update(@Valid @RequestBody StoryRequestTo dto)throws ResponseStatusException{
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") BigInteger id) throws ResponseStatusException {

        service.delete(id);
    }


}
