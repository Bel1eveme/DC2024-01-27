package by.rusakovich.publisher.note.spi.kafka;

import by.rusakovich.publisher.note.model.NoteKafkaRequestTO;
import by.rusakovich.publisher.note.model.NoteResponseTO;

import java.util.List;


public record NoteEvent(Operation operation, NoteKafkaRequestTO request, List<NoteResponseTO> responses) {
    public NoteEvent(Operation operation) {
        this(operation, null, null);
    }

    public NoteEvent(Operation operation, NoteKafkaRequestTO noteRequestTO) {
        this(operation, noteRequestTO, null);
    }

    public enum Operation {
        FIND_ALL,
        FIND_BY_ID,
        CREATE,
        UPDATE,
        REMOVE_BY_ID
    }
}
