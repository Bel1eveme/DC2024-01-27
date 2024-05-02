package by.bsuir.taskrest.controller;

import by.bsuir.taskrest.dto.request.StoryRequestTo;
import by.bsuir.taskrest.dto.response.StoryResponseTo;
import by.bsuir.taskrest.service.StoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/storys")
public class StoryController {

    private final StoryService storyService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StoryResponseTo> getAllStories(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return storyService.getAllStories(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StoryResponseTo getStoryById(@PathVariable Long id) {
        return storyService.getStoryById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoryResponseTo createStory(@Valid @RequestBody StoryRequestTo story) {
        return storyService.createStory(story);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public StoryResponseTo updateStory(@Valid @RequestBody StoryRequestTo story) {
        return storyService.updateStory(story);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StoryResponseTo updateStory(@PathVariable Long id, @Valid @RequestBody StoryRequestTo story) {
        return storyService.updateStory(id, story);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStory(@PathVariable Long id) {
        storyService.deleteStory(id);
    }
}
