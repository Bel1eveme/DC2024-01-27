package com.example.discussion.mapper;

import com.example.discussion.dto.NoteRequestTo;
import com.example.discussion.dto.NoteResponseTo;
import com.example.discussion.model.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    Note noteRequestToNote(NoteRequestTo noteRequestTo);
    NoteResponseTo noteToNoteResponse(Note note);
}
