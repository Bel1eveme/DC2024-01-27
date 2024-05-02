package by.rusakovich.discussion.spi.kafka;

import by.rusakovich.discussion.model.NoteIternalRequestTO;
import by.rusakovich.discussion.model.NoteResponseTO;
import by.rusakovich.discussion.service.NoteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@EnableKafka
@Component
@RequiredArgsConstructor
public class KafkaServer {

    @Value("${api.kafka.response-topic}")
    private String RESPONSE_TOPIC;

    private final KafkaTemplate<String, String> sender;
    private final ObjectMapper jsonMapper;
    private final NoteService noteService;

    @KafkaListener(topics = "${api.kafka.request-topic}", groupId = "${api.kafka.request-topic}")
    private void listenNote(ConsumerRecord<String, String> record) {
        UUID uuid = UUID.fromString(record.key());
        try {
            NoteEvent noteEvent = jsonMapper.readValue(record.value(), NoteEvent.class);
            NoteIternalRequestTO noteRequest = noteEvent.request();
            List<NoteResponseTO> response = switch (noteEvent.operation()){
                case CREATE ->List.of(noteService.create(noteRequest));
                case UPDATE -> List.of(noteService.update(noteRequest));
                case FIND_ALL -> noteService.readAll();
                case FIND_BY_ID -> List.of(noteService.readById(noteRequest.id()));
                case REMOVE_BY_ID -> {
                    noteService.deleteById(noteRequest.id());
                    yield null;
                }

            };
            NoteEvent result = new NoteEvent(response);
            sender.send(RESPONSE_TOPIC, uuid.toString(), jsonMapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}