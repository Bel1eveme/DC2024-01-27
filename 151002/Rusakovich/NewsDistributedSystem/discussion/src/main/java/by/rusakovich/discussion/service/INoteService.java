package by.rusakovich.discussion.service;

import by.rusakovich.discussion.model.NoteIternalRequestTO;
import by.rusakovich.discussion.model.NoteResponseTO;

import java.util.List;


public interface INoteService {
    NoteResponseTO readById(Long id);
    List<NoteResponseTO> readAll();
    NoteResponseTO create(NoteIternalRequestTO newEntity);
    NoteResponseTO update(NoteIternalRequestTO updatedEntity);
    void deleteById(Long id);
}
