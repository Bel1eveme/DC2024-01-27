package org.example.discussion.impl.note.mapper.Impl;


import org.example.discussion.impl.note.Note;
import org.example.discussion.impl.note.dto.NoteRequestTo;
import org.example.discussion.impl.note.dto.NoteResponseTo;
import org.example.discussion.impl.note.mapper.NoteMapper;

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
                note.getIssueId(),
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
    public Note dtoToEntity(NoteRequestTo noteRequestTo, String country) {
        return new Note(
                noteRequestTo.getId(),
                noteRequestTo.getIssueId(),
                country,
                noteRequestTo.getContent()
        );
    }

    @Override
    public NoteResponseTo noteToResponseTo(Note note) {
        return new NoteResponseTo(
                note.getId(),
                note.getIssueId(),
                note.getContent());
    }

    @Override
    public List<NoteResponseTo> noteToResponseTo(Iterable<Note> notes) {
        return StreamSupport.stream(notes.spliterator(), false)
                .map(this::noteToResponseTo)
                .collect(Collectors.toList());
    }
}
