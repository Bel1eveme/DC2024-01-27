package org.example.publisher.api.Controllers;

import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.impl.creator.dto.CreatorRequestTo;
import org.example.publisher.impl.creator.dto.CreatorResponseTo;
import org.example.publisher.impl.creator.service.CreatorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/creators")
public class CreatorController {

    private final CreatorService creatorService;

    public CreatorController(CreatorService creatorService) {
        this.creatorService = creatorService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<CreatorResponseTo> getCreators() {
        return creatorService.getCreators();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    CreatorResponseTo getCreatorById(@PathVariable BigInteger id) throws EntityNotFoundException {
        return creatorService.getCreatorById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreatorResponseTo makeCreator(@Valid @RequestBody CreatorRequestTo creatorRequestTo) throws DuplicateEntityException {
        return creatorService.createCreator(creatorRequestTo);
    }

    @PutMapping
    CreatorResponseTo updateCreator(@Valid @RequestBody CreatorRequestTo creatorRequestTo) throws EntityNotFoundException{
        return creatorService.updateCreator(creatorRequestTo);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    void deleteCreator(@PathVariable BigInteger id) throws EntityNotFoundException {
        creatorService.deleteCreator(id);
    }
}
