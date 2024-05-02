package org.example.discussion.api.Controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.discussion.api.exception.DuplicateEntityException;
import org.example.discussion.api.exception.EntityNotFoundException;
import org.example.discussion.impl.note.dto.NoteRequestTo;
import org.example.discussion.impl.note.dto.NoteResponseTo;
import org.example.discussion.impl.note.service.NoteService;
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
