package com.example.publicator.controller;


import com.example.publicator.dto.CreatorRequestTo;
import com.example.publicator.dto.CreatorResponseTo;
import com.example.publicator.service.CreatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/")
public class CreatorController {
    @Autowired
    CreatorService creatorService;

    @PostMapping(value = "creators")
    public ResponseEntity<?> create(@RequestBody CreatorRequestTo creator) {
        CreatorResponseTo cr = creatorService.create(creator);
        return new ResponseEntity<>(cr, HttpStatus.CREATED);
    }

    @GetMapping(value = "creators", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<CreatorResponseTo>> read(
            @RequestParam(required = false, defaultValue = "0") Integer pageInd,
            @RequestParam(required = false, defaultValue = "20") Integer numOfElem,
            @RequestParam(required = false, defaultValue = "id") String sortedBy,
            @RequestParam(required = false, defaultValue = "desc") String direction)
    {
        final List<CreatorResponseTo> creators = creatorService.readAll(pageInd, numOfElem, sortedBy, direction);
        return new ResponseEntity<>(creators, HttpStatus.OK);
    }

    @GetMapping(value = "creators/{id}")
    public ResponseEntity<CreatorResponseTo> read(@PathVariable(name = "id") int id) {
        final CreatorResponseTo creator = creatorService.read(id);
        return new ResponseEntity<>(creator, HttpStatus.OK);
    }

    @PutMapping(value = "creators")
    public ResponseEntity<?> update(@RequestBody CreatorRequestTo creator) {
        final CreatorResponseTo updated = creatorService.update(creator, creator.getId());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping(value = "creators/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        final boolean deleted = creatorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
