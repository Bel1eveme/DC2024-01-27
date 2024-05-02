package com.example.rv.impl.note.service;

import com.example.rv.api.exception.DuplicateEntityException;
import com.example.rv.api.exception.EntityNotFoundException;
import com.example.rv.api.repository.CrudRepository;
import com.example.rv.impl.note.Note;
import com.example.rv.impl.note.NoteRepository;
import com.example.rv.impl.note.dto.NoteRequestTo;
import com.example.rv.impl.note.dto.NoteResponseTo;
import com.example.rv.impl.note.mapper.Impl.NoteMapperImpl;
import com.example.rv.impl.tweet.Tweet;
import com.example.rv.impl.tweet.TweetRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final TweetRepository tweetRepository;

    private final NoteMapperImpl noteMapper;

    private final String ENTITY_NAME = "note";

    public List<NoteResponseTo> getNotes(){
        List<Note> notes = noteRepository.findAll();
        List<NoteResponseTo> notesTo = new ArrayList<>();
        for (var item : notes) {
            notesTo.add(noteMapper.noteToResponseTo(item));
        }

        return notesTo;
    }

    public NoteResponseTo getNoteById(BigInteger id) throws EntityNotFoundException {
        Optional<Note> note = noteRepository.findById(id);
        if(note.isEmpty()){
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        return noteMapper.noteToResponseTo(note.get());
    }

    public NoteResponseTo saveNote(NoteRequestTo noteTO) throws EntityNotFoundException, DuplicateEntityException {
        Optional<Tweet> tweet = tweetRepository.findById(noteTO.getTweetId());
        if (tweet.isEmpty()){
            throw new EntityNotFoundException("tweet", noteTO.getTweetId());
        }
        try {
            Note note = noteRepository.save(noteMapper.dtoToEntity(noteTO, tweet.get()));
            return noteMapper.noteToResponseTo(note);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateEntityException(ENTITY_NAME, "");
        }
    }

    public NoteResponseTo updateNote(NoteRequestTo noteTO) throws EntityNotFoundException, DuplicateEntityException {
        if (noteRepository.findById(noteTO.getId()).isEmpty()){
            throw new EntityNotFoundException(ENTITY_NAME, noteTO.getId());
        }
        Optional<Tweet> tweet = tweetRepository.findById(noteTO.getTweetId());
        if (tweet.isEmpty()){
            throw new EntityNotFoundException("tweet", noteTO.getTweetId());
        }
        try {
            Note note = noteRepository.save(noteMapper.dtoToEntity(noteTO, tweet.get()));
            return noteMapper.noteToResponseTo(note);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateEntityException(ENTITY_NAME, "");
        }
    }

    public void deleteNote(BigInteger id) throws EntityNotFoundException{
        if(noteRepository.findById(id).isEmpty()){
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        noteRepository.deleteById(id);
    }
}
