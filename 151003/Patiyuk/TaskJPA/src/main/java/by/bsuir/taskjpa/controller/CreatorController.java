package by.bsuir.taskrest.controller;

import by.bsuir.taskrest.dto.request.CreatorRequestTo;
import by.bsuir.taskrest.dto.response.CreatorResponseTo;
import by.bsuir.taskrest.service.CreatorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/creators")
public class CreatorController {

    private final CreatorService creatorService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CreatorResponseTo> getAllCreators(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return creatorService.getAllCreators(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreatorResponseTo getCreatorById(@PathVariable Long id) {
        return creatorService.getCreatorById(id);
    }

    @GetMapping("/story/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreatorResponseTo getCreatorByStoryId(@PathVariable Long id) {
        return creatorService.getCreatorByStoryId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatorResponseTo createCreator(@Valid @RequestBody CreatorRequestTo creator) {
        return creatorService.createCreator(creator);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public CreatorResponseTo updateCreator(@Valid @RequestBody CreatorRequestTo creator) {
        return creatorService.updateCreator(creator);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreatorResponseTo updateCreator(@PathVariable Long id, @Valid @RequestBody CreatorRequestTo creator) {
        return creatorService.updateCreator(id, creator);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCreator(@PathVariable Long id) {
        creatorService.deleteCreator(id);
    }
}
