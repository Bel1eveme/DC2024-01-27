package org.example.publisher.impl.note.mapper.Impl;


import org.example.publisher.impl.issue.Issue;
import org.example.publisher.impl.note.Note;
import org.example.publisher.impl.note.dto.NoteAddedResponseTo;
import org.example.publisher.impl.note.dto.NoteRequestTo;
import org.example.publisher.impl.note.dto.NoteResponseTo;
import org.example.publisher.impl.note.mapper.NoteMapper;
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
                note.getIssue().getId(),
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
    public Note dtoToEntity(NoteRequestTo noteRequestTo, Issue issue) {
        return new Note(
                noteRequestTo.getId(),
                issue,
                noteRequestTo.getContent()
        );
    }

    @Override
    public NoteResponseTo noteToResponseTo(Note note) {
        return new NoteResponseTo(
                note.getId(),
                note.getIssue().getId(),
                note.getContent());
    }

    @Override
    public NoteAddedResponseTo noteToAddedResponesTo(NoteRequestTo note, String status) {
        return new NoteAddedResponseTo(
            note.getId(),
            note.getIssueId(),
            note.getContent(),
            status
        );
    }

    @Override
    public List<NoteResponseTo> noteToResponseTo(Iterable<Note> notes) {
        return StreamSupport.stream(notes.spliterator(), false)
                .map(this::noteToResponseTo)
                .collect(Collectors.toList());
    }
}
