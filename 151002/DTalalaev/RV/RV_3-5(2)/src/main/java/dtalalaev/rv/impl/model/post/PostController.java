package dtalalaev.rv.impl.model.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping({"/posts"})
public class PostController {

    @Autowired
    private PostService service;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Post> findAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponseTo findOne(@PathVariable("id") BigInteger id)throws ResponseStatusException{
        return service.findOne(id);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponseTo create(@Valid @RequestBody PostRequestTo dto){
        return service.create(dto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public PostResponseTo update(@Valid @RequestBody PostRequestTo dto)throws ResponseStatusException{
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") BigInteger id) throws ResponseStatusException {
        service.delete(id);
    }


}
