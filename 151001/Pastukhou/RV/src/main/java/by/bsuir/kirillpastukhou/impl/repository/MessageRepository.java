package by.bsuir.kirillpastukhou.impl.repository;

import by.bsuir.kirillpastukhou.api.InMemoryRepository;
import by.bsuir.kirillpastukhou.impl.bean.Message;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MessageRepository implements InMemoryRepository<Message> {
    private final Map<Long, Message> MessageMemory = new HashMap<>();

    @Override
    public Message get(long id) {
        Message message = MessageMemory.get(id);
        if (message != null) {
            message.setId(id);
        }
        return message;
    }

    @Override
    public List<Message> getAll() {
        List<Message> messageList = new ArrayList<>();
        for (Long key : MessageMemory.keySet()) {
            Message message = MessageMemory.get(key);
            message.setId(key);
            messageList.add(message);
        }
        return messageList;
    }

    @Override
    public Message delete(long id) {
        return MessageMemory.remove(id);
    }

    @Override
    public Message insert(Message insertObject) {
        MessageMemory.put(insertObject.getId(), insertObject);
        return insertObject;
    }

    @Override
    public boolean update(Message updatingValue) {
        return MessageMemory.replace(updatingValue.getId(), MessageMemory.get(updatingValue.getId()), updatingValue);
    }


}
