package com.example.discussion.service;

import com.example.discussion.dto.NoteRequestTo;
import com.example.discussion.dto.NoteResponseTo;
import com.example.discussion.exception.NotFoundException;
import com.example.discussion.mapper.NoteListMapper;
import com.example.discussion.mapper.NoteMapper;
import com.example.discussion.model.Note;
import com.example.discussion.repository.NoteRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Service
@Validated
public class NoteService {
    @Autowired
    //NoteDao noteDao;
    NoteRepository noteRepository;
    @Autowired
    NoteMapper noteMapper;
    @Autowired
    NoteListMapper noteListMapper;

    public NoteResponseTo create(@Valid NoteRequestTo noteRequestTo, String country){
        Note note = noteMapper.noteRequestToNote(noteRequestTo);
        note.setId(getId());
        note.setCountry(getCountry(country));
        return noteMapper.noteToNoteResponse(noteRepository.save(note));
    }

    public List<NoteResponseTo> readAll(){
        List<Note> res = noteRepository.findAll();
        return noteListMapper.toNoteResponseList(res);
    }

    public NoteResponseTo read(@Min(0) int id) throws NotFoundException {
        Optional<Note> note = noteRepository.findById(id).stream().findFirst();
        if(note.isPresent()){
            return noteMapper.noteToNoteResponse(note.get());
        }
        else
            throw new NotFoundException("Note not found", 404);
    }
    public NoteResponseTo update(@Valid NoteRequestTo noteRequestTo, String country) throws NotFoundException {
        Note note = noteMapper.noteRequestToNote(noteRequestTo);
        note.setId(noteRequestTo.getId());
        note.setCountry(getCountry(country));
        Optional<Note> res = noteRepository.findById(noteRequestTo.getId()).stream().findFirst();
        if(res.isPresent()){
            noteRepository.deleteByCountryAndIssueIdAndId(res.get().getCountry(), res.get().getIssueId(), res.get().getId());
            return noteMapper.noteToNoteResponse(noteRepository.save(note));
        }
        else
            throw new NotFoundException("Note not found", 404);
    }
    public boolean delete(@Min(0) int id) throws NotFoundException {
        Optional<Note> note = noteRepository.findById(id).stream().findFirst();
        if(note.isPresent()){
            noteRepository.deleteByCountryAndIssueIdAndId(note.get().getCountry(), note.get().getIssueId(), note.get().getId());
            return true;
        }
        else
            throw new NotFoundException("Note not found", 404);
    }


    private int getId(){
        int currentSecond = (int) (System.currentTimeMillis() / 1000);

        int shiftedTime = currentSecond << 10;

        int randomBits = new Random().nextInt(1 << 10);

        return Math.abs(shiftedTime | randomBits);
    }
    private String getCountry(String requestHeader){
        Map<String, Double> languageMap = getStringDoubleMap(requestHeader);
        Map<String, Double> loadMap = new HashMap<>();
        for (String country: languageMap.keySet()){
            loadMap.put(country, noteRepository.countByCountry(country)*(1-languageMap.get(country)));
        }
        double minValue = Double.MAX_VALUE;
        String minKey = null;

        for (Map.Entry<String, Double> entry : loadMap.entrySet()) {
            if (entry.getValue() < minValue) {
                minValue = entry.getValue();
                minKey = entry.getKey();
            }
        }
        return minKey;
    }

    private static Map<String, Double> getStringDoubleMap(String requestHeader) {
        String[] languages = requestHeader.split(",");
        Map<String, Double> languageMap = new HashMap<>();
        for (String language : languages) {
            String[] parts = language.split(";");
            String lang = parts[0].trim();
            double priority = 1.0; // По умолчанию
            if (parts.length > 1) {
                String[] priorityParts = parts[1].split("=");
                priority = Double.parseDouble(priorityParts[1]);
            }
            languageMap.put(lang, priority);
        }
        return languageMap;
    }
}
