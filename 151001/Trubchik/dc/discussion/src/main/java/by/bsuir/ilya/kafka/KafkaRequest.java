package by.bsuir.ilya.kafka;

import by.bsuir.ilya.dto.PostRequestTo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KafkaRequest {
    private UUID key;

    private KafkaMethods requestMethod;

    private List<PostRequestTo> dtoToTransfer = new ArrayList<>();

    public KafkaMethods getRequestMethod() {
        return requestMethod;
    }

    public List<PostRequestTo> getDtoToTransfer() {
        return dtoToTransfer;
    }

    public UUID getKey() {
        return key;
    }

    public void setRequestMethod(KafkaMethods requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void setDtoToTransfer(List<PostRequestTo> dtoToTransfer) {
        this.dtoToTransfer = dtoToTransfer;
    }

    public void setKey(UUID key) {
        this.key = key;
    }
}
