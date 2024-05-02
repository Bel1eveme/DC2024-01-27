package com.example.rv.api.Controllers;

import com.example.rv.api.exception.DuplicateEntityException;
import com.example.rv.api.exception.EntityNotFoundException;
import com.example.rv.impl.note.dto.NoteRequestTo;
import com.example.rv.impl.note.dto.NoteResponseTo;
import com.example.rv.impl.note.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NoteResponseTo> getNotes() {
        return noteService.getNotes();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NoteResponseTo getNoteById(@PathVariable BigInteger id) throws EntityNotFoundException {
        return noteService.getNoteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponseTo saveNote(@Valid @RequestBody NoteRequestTo noteTo) throws DuplicateEntityException, EntityNotFoundException {
        return noteService.saveNote(noteTo);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public NoteResponseTo updateNote(@Valid @RequestBody NoteRequestTo noteTo) throws DuplicateEntityException, EntityNotFoundException {
        return noteService.saveNote(noteTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable BigInteger id) throws EntityNotFoundException {
        noteService.deleteNote(id);
    }
}
