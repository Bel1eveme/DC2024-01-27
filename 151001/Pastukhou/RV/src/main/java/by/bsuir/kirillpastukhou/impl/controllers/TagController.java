package by.bsuir.kirillpastukhou.impl.controllers;

import by.bsuir.kirillpastukhou.impl.service.TagService;
import by.bsuir.kirillpastukhou.impl.dto.TagRequestTo;
import by.bsuir.kirillpastukhou.impl.dto.TagResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public ResponseEntity<List<TagResponseTo>> getAllTags() {
        List<TagResponseTo> tagResponseToList = tagService.getAll();
        return new ResponseEntity<>(tagResponseToList, HttpStatus.OK);
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<TagResponseTo> getTag(@PathVariable long id) {
        TagResponseTo TagResponseTo = tagService.get(id);
        return new ResponseEntity<>(TagResponseTo, TagResponseTo == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping("/tags")
    public ResponseEntity<TagResponseTo> createTag(@RequestBody TagRequestTo TagTo) {
        TagResponseTo addedTag = tagService.add(TagTo);
        return new ResponseEntity<>(addedTag, HttpStatus.CREATED);
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<TagResponseTo> deleteTag(@PathVariable long id) {
        TagResponseTo deletedTag = tagService.delete(id);
        return new ResponseEntity<>(deletedTag, deletedTag == null ? HttpStatus.NOT_FOUND : HttpStatus.NO_CONTENT);
    }

    @PutMapping("/tags")
    public ResponseEntity<TagResponseTo> updateTag(@RequestBody TagRequestTo TagRequestTo) {
        TagResponseTo TagResponseTo = tagService.update(TagRequestTo);
        return new ResponseEntity<>(TagResponseTo, TagResponseTo.getName() == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
}
