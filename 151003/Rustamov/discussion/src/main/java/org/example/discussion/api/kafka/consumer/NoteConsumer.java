package org.example.discussion.api.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.discussion.api.exception.DuplicateEntityException;
import org.example.discussion.api.exception.EntityNotFoundException;
import org.example.discussion.api.kafka.producer.NoteProducer;
import org.example.discussion.impl.note.dto.NoteRequestTo;
import org.example.discussion.impl.note.dto.NoteResponseTo;
import org.example.discussion.impl.note.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class NoteConsumer {
    private final NoteService noteService;
    private final NoteProducer noteProducer;
    private final String INPUT_TOPIC = "OutTopic";

    @Autowired
    public NoteConsumer(NoteService noteService, NoteProducer noteProducer) {
        this.noteService = noteService;
        this.noteProducer = noteProducer;
    }

    @KafkaListener(topics = INPUT_TOPIC, groupId = "group")
    public void listen(String message) throws EntityNotFoundException, JsonProcessingException, DuplicateEntityException {
        String requestId = message.substring(0, message.indexOf(","));
        if (message.contains("get:")) {
            String id = message.substring(message.indexOf(":") + 1);
            NoteResponseTo note = noteService.getNoteById(new BigInteger(id));
            noteProducer.sendResponseNote(note, requestId);
        } else if (message.contains("post:")) {
            String noteString = message.substring(message.indexOf(":") + 1);
            noteService.saveNote(convertJsonToNoteRequestTo(noteString));
        } else if (message.contains("put:")) {
            String noteString = message.substring(message.indexOf(":") + 1);
            NoteResponseTo note = noteService.updateNote(convertJsonToNoteRequestTo(noteString));
            noteProducer.sendResponseNote(note, requestId);
        } else if (message.contains("delete:")) {
            String id = message.substring(message.indexOf(":") + 1);
            noteService.deleteNote(new BigInteger(id));
        }
    }

    private NoteRequestTo convertJsonToNoteRequestTo(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, NoteRequestTo.class);
    }
}
