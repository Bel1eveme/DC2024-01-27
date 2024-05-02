package by.rusakovich.publisher.note;

import by.rusakovich.publisher.note.model.NoteKafkaRequestTO;
import by.rusakovich.publisher.note.spi.kafka.KafkaClient;
import by.rusakovich.publisher.note.spi.kafka.NoteEvent;
import by.rusakovich.publisher.generics.IEntityService;
import by.rusakovich.publisher.note.model.NoteResponseTO;
import by.rusakovich.publisher.note.spi.redis.NoteRedisClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NoteService implements IEntityService<Long, NoteKafkaRequestTO, NoteResponseTO> {
    private final KafkaClient kafkaClient;
    private final NoteRedisClient noteRedisClient;

    @Override
    public NoteResponseTO readById(Long id) {
        // here we can pass lang too
        var response = noteRedisClient.get(id);
        if(response == null){
            NoteEvent noteEvent = new NoteEvent(NoteEvent.Operation.FIND_BY_ID, new NoteKafkaRequestTO(null, id, null, null));
            NoteEvent result = kafkaClient.sync(noteEvent);
            response = result.responses().getFirst();
            noteRedisClient.put(response.id(), response);
        }
        return response;
    }

    @Override
    public List<NoteResponseTO> readAll() {
        NoteEvent noteEvent = new NoteEvent(NoteEvent.Operation.FIND_ALL);
        NoteEvent result= kafkaClient.sync(noteEvent);
        return result.responses();
    }


    @Override
    public NoteResponseTO create(NoteKafkaRequestTO newEntity) {
        NoteEvent noteEvent = new NoteEvent(NoteEvent.Operation.CREATE, newEntity);
        NoteEvent resultEvent = kafkaClient.sync(noteEvent);
        var response = resultEvent.responses().getFirst();
        if(response != null){
            noteRedisClient.put(response.id(), response);
        }
        return response;
    }

    @Override
    public NoteResponseTO update(NoteKafkaRequestTO updatedEntity) {
        // validation on newsId break tests
        NoteEvent noteEvent = new NoteEvent(NoteEvent.Operation.UPDATE, updatedEntity);
        NoteEvent resultEvent = kafkaClient.sync(noteEvent);
        var response = resultEvent.responses().getFirst();
        if(response != null){
            noteRedisClient.put(response.id(), response);
        }
        return response;
    }

    @Override
    public void deleteById(Long id) {
        NoteEvent noteEvent = new NoteEvent(NoteEvent.Operation.REMOVE_BY_ID, new NoteKafkaRequestTO(null, id, null, null));
        // how get result?
        kafkaClient.sync(noteEvent);
        noteRedisClient.delete(id);
    }
}
