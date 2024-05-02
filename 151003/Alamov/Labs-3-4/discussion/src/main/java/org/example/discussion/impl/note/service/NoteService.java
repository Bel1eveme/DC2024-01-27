package org.example.discussion.impl.note.service;

import lombok.RequiredArgsConstructor;
import org.example.discussion.api.exception.DuplicateEntityException;
import org.example.discussion.api.exception.EntityNotFoundException;
import org.example.discussion.impl.note.Note;
import org.example.discussion.impl.note.NoteRepository;
import org.example.discussion.impl.note.dto.NoteRequestTo;
import org.example.discussion.impl.note.dto.NoteResponseTo;
import org.example.discussion.impl.note.mapper.Impl.NoteMapperImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    private final NoteMapperImpl noteMapper;
    private final String ENTITY_NAME = "note";

    private final String TWEET_PATH = "http://localhost:24110/api/v1.0/tweets/";


    public List<NoteResponseTo> getNotes() {
        List<Note> notes = noteRepository.findAll();
        List<NoteResponseTo> notesTo = new ArrayList<>();
        for (var item : notes) {
            notesTo.add(noteMapper.noteToResponseTo(item));
        }

        return notesTo;
    }

    public NoteResponseTo getNoteById(BigInteger id) throws EntityNotFoundException {
        Optional<Note> note = noteRepository.findBy_id("local", id);
        if (note.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        return noteMapper.noteToResponseTo(note.get());
    }

    public NoteResponseTo saveNote(NoteRequestTo noteTO) throws EntityNotFoundException, DuplicateEntityException {

        Note note = noteRepository.save(noteMapper.dtoToEntity(noteTO, "local"));
        return noteMapper.noteToResponseTo(note);

    }

    public NoteResponseTo updateNote(NoteRequestTo noteTO) throws EntityNotFoundException, DuplicateEntityException {
        Note note = noteRepository.save(noteMapper.dtoToEntity(noteTO, "local"));
        return noteMapper.noteToResponseTo(note);

    }

    public void deleteNote(BigInteger id) throws EntityNotFoundException {
        if (noteRepository.findBy_id("local", id).isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        noteRepository.deleteBy_id("local", id);
    }
}
