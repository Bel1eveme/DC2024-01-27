package by.rusakovich.publisher.note.spi.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@EnableKafka
@Component
@RequiredArgsConstructor
public class KafkaClient {

    @Value("${api.kafka.request-topic}")
    private String REQUEST_TOPIC;

    private final KafkaTemplate<String, String> sender;
    private final ObjectMapper jsonMapper;
    private final ConcurrentHashMap<UUID, Exchanger<NoteEvent>> kafkaCache = new ConcurrentHashMap<>();

    public NoteEvent sync(NoteEvent noteEvent) {
        UUID uuid = UUID.randomUUID();
        Exchanger<NoteEvent> exchanger = new Exchanger<>();
        kafkaCache.put(uuid, exchanger);
        try{
            sender.send(REQUEST_TOPIC, uuid.toString(), jsonMapper.writeValueAsString(noteEvent));
            return exchanger.exchange(noteEvent, 1, TimeUnit.SECONDS);
        } catch (JsonProcessingException | InterruptedException | TimeoutException e) {
            kafkaCache.remove(uuid);
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "${api.kafka.response-topic}", groupId = "groupId=#{T(java.util.UUID).randomUUID().toString()}")
    private void listenNote(ConsumerRecord<String, String> record) {
        UUID uuid = UUID.fromString(record.key());
        try {
            NoteEvent result = jsonMapper.readValue(record.value(), NoteEvent.class);
            Exchanger<NoteEvent> exchanger = kafkaCache.remove(uuid);
            if(exchanger != null) {
                exchanger.exchange(result, 1, TimeUnit.SECONDS);
            }
        } catch (JsonProcessingException | TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}