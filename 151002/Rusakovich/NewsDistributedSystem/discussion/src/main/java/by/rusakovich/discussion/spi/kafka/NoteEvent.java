package by.rusakovich.discussion.spi.kafka;


import by.rusakovich.discussion.model.NoteIternalRequestTO;
import by.rusakovich.discussion.model.NoteResponseTO;

import java.util.List;


public record NoteEvent(Operation operation, NoteIternalRequestTO request, List<NoteResponseTO> responses) {

    public NoteEvent(List<NoteResponseTO> responses) {
        this(null, null, responses);
    }

    public enum Operation {
        FIND_ALL,
        FIND_BY_ID,
        CREATE,
        UPDATE,
        REMOVE_BY_ID
    }
}
