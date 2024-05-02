package com.example.rv.impl.note.mapper.Impl;


import com.example.rv.impl.note.Note;
import com.example.rv.impl.note.dto.NoteRequestTo;
import com.example.rv.impl.note.dto.NoteResponseTo;
import com.example.rv.impl.note.mapper.NoteMapper;
import com.example.rv.impl.tweet.Tweet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class NoteMapperImpl implements NoteMapper {
    @Override
    public NoteRequestTo noteToRequestTo(Note note) {
        return new NoteRequestTo(
                note.getId(),
                note.getTweet().getId(),
                note.getContent()
        );
    }

    @Override
    public List<NoteRequestTo> noteToRequestTo(Iterable<Note> notes) {
        return StreamSupport.stream(notes.spliterator(), false)
                .map(this::noteToRequestTo)
                .collect(Collectors.toList());
    }

    @Override
    public Note dtoToEntity(NoteRequestTo noteRequestTo, Tweet tweet) {
        return new Note(
                noteRequestTo.getId(),
                tweet,
                noteRequestTo.getContent()
        );
    }

    @Override
    public NoteResponseTo noteToResponseTo(Note note) {
        return new NoteResponseTo(
                note.getId(),
                note.getTweet().getId(),
                note.getContent());
    }

    @Override
    public List<NoteResponseTo> noteToResponseTo(Iterable<Note> notes) {
        return StreamSupport.stream(notes.spliterator(), false)
                .map(this::noteToResponseTo)
                .collect(Collectors.toList());
    }
}
