package dtalalaev.rv.impl.model.tag;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
@Data
@RestController
@RequestMapping("/tags")
public class TagController {

    private TagService service;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> findAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagResponseTo findOne(@PathVariable("id") BigInteger id) throws ResponseStatusException {
        return service.findOne(id);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponseTo create(@Valid @RequestBody TagRequestTo dto){
        return service.create(dto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public TagResponseTo update(@Valid @RequestBody TagRequestTo dto) throws ResponseStatusException{
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") BigInteger id) throws ResponseStatusException {
        service.delete(id);
    }

}
