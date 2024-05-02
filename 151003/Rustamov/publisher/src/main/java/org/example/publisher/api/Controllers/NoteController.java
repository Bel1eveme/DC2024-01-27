package org.example.publisher.api.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.impl.note.dto.NoteAddedResponseTo;
import org.example.publisher.impl.note.dto.NoteRequestTo;
import org.example.publisher.impl.note.dto.NoteResponseTo;
import org.example.publisher.impl.note.service.NoteService;
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
    public NoteResponseTo getNoteById(@PathVariable BigInteger id) throws EntityNotFoundException, InterruptedException {
        return noteService.getNoteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteAddedResponseTo saveNote(@Valid @RequestBody NoteRequestTo noteTo) throws DuplicateEntityException, EntityNotFoundException, InterruptedException {
        return noteService.saveNote(noteTo);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public NoteResponseTo updateNote(@Valid @RequestBody NoteRequestTo noteTo) throws DuplicateEntityException, EntityNotFoundException, JsonProcessingException, InterruptedException {
        return noteService.updateNote(noteTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable BigInteger id) throws EntityNotFoundException, InterruptedException {
        noteService.deleteNote(id);
    }
}
