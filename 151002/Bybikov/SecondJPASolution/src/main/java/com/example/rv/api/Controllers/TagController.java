package com.example.rv.api.Controllers;

import com.example.rv.api.exception.DuplicateEntityException;
import com.example.rv.api.exception.EntityNotFoundException;
import com.example.rv.impl.note.dto.NoteRequestTo;
import com.example.rv.impl.note.dto.NoteResponseTo;
import com.example.rv.impl.tag.*;
import com.example.rv.impl.tag.Service.TagService;
import com.example.rv.impl.tag.dto.TagRequestTo;
import com.example.rv.impl.tag.dto.TagResponseTo;
import com.example.rv.impl.tag.mapper.Impl.TagMapperImpl;
import com.example.rv.impl.tag.mapper.TagMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/v1.0/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TagResponseTo> getTags() {
        return tagService.getTags();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagResponseTo getTagById(@PathVariable BigInteger id) throws EntityNotFoundException {
        return tagService.getTagById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponseTo saveTag(@Valid @RequestBody TagRequestTo tagTo) throws DuplicateEntityException, EntityNotFoundException {
        return tagService.saveTag(tagTo);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public TagResponseTo updateNote(@Valid @RequestBody TagRequestTo tagTo) throws DuplicateEntityException, EntityNotFoundException {
        return tagService.updateTag(tagTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable BigInteger id) throws EntityNotFoundException {
        tagService.deleteTagById(id);
    }

}
