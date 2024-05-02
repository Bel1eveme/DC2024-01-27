package dtalalaev.rv.impl.model.creator;


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
@RequestMapping("/creators")
public class CreatorController {

    private CreatorService service;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Creator> findAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreatorResponseTo findOne(@PathVariable("id") BigInteger id) throws ResponseStatusException {
        return service.findOne(id);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatorResponseTo create(@Valid @RequestBody CreatorRequestTo dto){
        return service.create(dto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public CreatorResponseTo update(@Valid @RequestBody CreatorRequestTo dto) throws ResponseStatusException{
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") BigInteger id) throws ResponseStatusException {
        service.delete(id);
    }
}
