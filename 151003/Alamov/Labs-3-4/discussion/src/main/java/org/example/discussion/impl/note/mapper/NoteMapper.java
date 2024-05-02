package org.example.discussion.impl.note.mapper;

import org.example.discussion.impl.note.Note;
import org.example.discussion.impl.note.dto.NoteRequestTo;
import org.example.discussion.impl.note.dto.NoteResponseTo;

import java.util.List;

public interface NoteMapper {
    NoteRequestTo noteToRequestTo(Note note);

    List<NoteRequestTo> noteToRequestTo(Iterable<Note> notes);

    Note dtoToEntity(NoteRequestTo noteRequestTo, String country);

    NoteResponseTo noteToResponseTo(Note note);

    List<NoteResponseTo> noteToResponseTo(Iterable<Note> notes);
}
