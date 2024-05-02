package org.example.publisher.impl.note.mapper;

import org.example.publisher.impl.note.Note;
import org.example.publisher.impl.note.dto.NoteAddedResponseTo;
import org.example.publisher.impl.note.dto.NoteRequestTo;
import org.example.publisher.impl.note.dto.NoteResponseTo;
import org.example.publisher.impl.tweet.Tweet;

import java.util.List;

public interface NoteMapper {
    NoteRequestTo noteToRequestTo(Note note);

    List<NoteRequestTo> noteToRequestTo(Iterable<Note> notes);

    Note dtoToEntity(NoteRequestTo noteRequestTo, Tweet tweet);

    NoteResponseTo noteToResponseTo(Note note);
    NoteAddedResponseTo noteToAddedResponesTo(NoteRequestTo note, String status);

    List<NoteResponseTo> noteToResponseTo(Iterable<Note> notes);
}
