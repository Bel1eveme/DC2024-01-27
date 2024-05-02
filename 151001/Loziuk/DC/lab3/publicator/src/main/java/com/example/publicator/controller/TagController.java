package com.example.publicator.controller;

import com.example.publicator.dto.TagRequestTo;
import com.example.publicator.dto.TagResponseTo;
import com.example.publicator.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/v1.0/")
public class TagController {
    @Autowired
    TagService tagService;

    @PostMapping(value = "tags")
    public ResponseEntity<?> create(@RequestBody TagRequestTo tagRequestTo) {
        TagResponseTo tag = tagService.create(tagRequestTo);
        return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    @GetMapping(value = "tags", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> read(
            @RequestParam(required = false, defaultValue = "0") Integer pageInd,
            @RequestParam(required = false, defaultValue = "20") Integer numOfElem,
            @RequestParam(required = false, defaultValue = "id") String sortedBy,
            @RequestParam(required = false, defaultValue = "desc") String direction)
    {
        final List<TagResponseTo> list = tagService.readAll(pageInd, numOfElem, sortedBy, direction);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "tags/{id}")
    public ResponseEntity<?> read(@PathVariable(name = "id") int id) {
        TagResponseTo tag = tagService.read(id);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @PutMapping(value = "tags")
    public ResponseEntity<?> update(@RequestBody TagRequestTo tagRequestTo) {
        TagResponseTo tag = tagService.update(tagRequestTo, tagRequestTo.getId());
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @DeleteMapping(value = "tags/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        boolean isDeleted = tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
