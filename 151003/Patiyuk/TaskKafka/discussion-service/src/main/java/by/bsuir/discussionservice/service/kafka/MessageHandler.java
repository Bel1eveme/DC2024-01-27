package by.bsuir.discussionservice.service.kafka;

import by.bsuir.discussionservice.dto.message.InTopicMessage;
import by.bsuir.discussionservice.dto.message.Operation;
import by.bsuir.discussionservice.dto.message.OutTopicMessage;
import by.bsuir.discussionservice.dto.request.MessageRequestTo;
import by.bsuir.discussionservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageHandler {
    private final MessageService messageService;
    private final KafkaProducerService kafkaProducerService;

    @Value("${spring.kafka.topic.response}")
    private String outTopicName;

    @Value("${spring.kafka.topic.create}")
    private String createTopicName;

    public void processMessage(InTopicMessage message, String key) {
        try {
            processMessageInternal(message, key);
        } catch (Exception e) {
            kafkaProducerService.sendMessage(
                    OutTopicMessage.builder()
                            .operation(message.operation())
                            .error(e.getMessage())
                            .build(),
                    key,
                    outTopicName
            );
        }
    }

    private void processMessageInternal(InTopicMessage message, String key) {
        switch (message.operation()) {
            case CREATE:
                createMessage(message.message(), key);
                break;
            case GET_ALL:
                getAllMessages(message.page(), message.size(), key);
                break;
            case GET_BY_ID:
                getMessageById(message.message().id(), key);
                break;
            case GET_BY_STORY_ID:
                getMessagesByStoryId(message.message().storyId(), message.page(), message.size(), key);
                break;
            case UPDATE:
                updateMessage(message.message(), key);
                break;
            case DELETE:
                deleteMessage(message.message().id(), key);
                break;
        }
    }

    private void createMessage(MessageRequestTo message, String key) {
        Optional.of(messageService.createMessage(message))
                .map(List::of)
                .map(messages -> OutTopicMessage.builder()
                        .operation(Operation.CREATE)
                        .result(messages)
                        .build())
                .ifPresent(outTopicMessage -> kafkaProducerService.sendMessage(outTopicMessage, key, outTopicName));
    }

    private void getAllMessages(Integer page, Integer size, String key) {
        Optional.of(messageService.getAllMessages(PageRequest.of(page, size)))
                .map(messages -> OutTopicMessage.builder()
                        .operation(Operation.GET_ALL)
                        .result(messages)
                        .build())
                .ifPresent(outTopicMessage -> kafkaProducerService.sendMessage(outTopicMessage, key, outTopicName));
    }

    private void getMessageById(Long id, String key) {
        Optional.of(messageService.getMessageById(id))
                .map(List::of)
                .map(messages -> OutTopicMessage.builder()
                        .operation(Operation.GET_BY_ID)
                        .result(messages)
                        .build())
                .ifPresent(outTopicMessage -> kafkaProducerService.sendMessage(outTopicMessage, key, outTopicName));
    }

    private void getMessagesByStoryId(Long id, Integer page, Integer size, String key) {
        Optional.of(messageService.getMessagesByStoryId(id, PageRequest.of(page, size)))
                .map(messages -> OutTopicMessage.builder()
                        .operation(Operation.GET_BY_STORY_ID)
                        .result(messages)
                        .build())
                .ifPresent(outTopicMessage -> kafkaProducerService.sendMessage(outTopicMessage, key, outTopicName));
    }

    private void updateMessage(MessageRequestTo message, String key) {
        Optional.of(messageService.updateMessage(message))
                .map(List::of)
                .map(messages -> OutTopicMessage.builder()
                        .operation(Operation.UPDATE)
                        .result(messages)
                        .build())
                .ifPresent(outTopicMessage -> kafkaProducerService.sendMessage(outTopicMessage, key, outTopicName));
    }

    private void deleteMessage(Long id, String key) {
        messageService.deleteMessage(id);
        kafkaProducerService.sendMessage(OutTopicMessage.builder()
                        .operation(Operation.DELETE)
                        .build(),
                key,
                outTopicName);
    }
}
