package by.bsuir.ilya.kafka;

import by.bsuir.ilya.Service.PostService;
import by.bsuir.ilya.dto.PostRequestTo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaMessaging {
    private final String LISTEN_TOPIC = "IN_TOPIC";
    private final String WRITE_TOPIC = "OUT_TOPIC";

    private ObjectMapper json = new JsonMapper();
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    PostService postService;

    @KafkaListener(topics=LISTEN_TOPIC,groupId = "group1")
    void listen(ConsumerRecord<String,String> consumerRecord)
    {
        KafkaRequest req = null;
        try {
            req = json.readValue(consumerRecord.value(), KafkaRequest.class);
            KafkaMethods requestMethod = req.getRequestMethod();
            switch (requestMethod) {
                case READ -> {
                    PostRequestTo result = postService.getByIdKafka(req.getDtoToTransfer().get(0).getId());
                    req.getDtoToTransfer().clear();
                    if(result!=null) {
                        req.getDtoToTransfer().add(0, result);
                    }
                    sendMessage(req);
                }
                case CREATE -> {

                    PostRequestTo result = postService.addKafka(req.getDtoToTransfer().get(0));
                    req.getDtoToTransfer().clear();
                    if(result!=null) {
                        req.getDtoToTransfer().add(0, result);
                    }
                    sendMessage(req);
                }
                case DELETE -> {
                    PostRequestTo result = postService.deleteByIdKafka(req.getDtoToTransfer().get(0).getId());
                    req.getDtoToTransfer().clear();
                    req.getDtoToTransfer().add(0,result);
                    sendMessage(req);
                }
                case UPDATE -> {
                    PostRequestTo result = postService.updateKafka(req.getDtoToTransfer().get(0));
                    req.getDtoToTransfer().clear();
                    req.getDtoToTransfer().add(0,result);
                    sendMessage(req);
                }
                case READ_ALL -> {
                    List<PostRequestTo> result = postService.getAllKafka();
                    req.setDtoToTransfer(result);
                    sendMessage(req);
                }
            }
        }
        catch (JsonProcessingException e) {
            return;
        }
    }

    void sendMessage(KafkaRequest response) throws JsonProcessingException
    {
        String responseString = json.writeValueAsString(response);
        ProducerRecord<String, String> record = new ProducerRecord<>(WRITE_TOPIC, response.getKey().toString(), responseString);
        kafkaTemplate.send(record);
    }
}
