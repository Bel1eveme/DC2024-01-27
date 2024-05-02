package com.example.discussion.mapper;

import com.example.discussion.dto.NoteRequestTo;
import com.example.discussion.dto.NoteResponseTo;
import com.example.discussion.model.Note;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = NoteMapper.class)
public interface NoteListMapper {
    List<Note> toNoteList(List<NoteRequestTo> noteRequestToList);

    List<NoteResponseTo> toNoteResponseList(List<Note> noteList);
}
