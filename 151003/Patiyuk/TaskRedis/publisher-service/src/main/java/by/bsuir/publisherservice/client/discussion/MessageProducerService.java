package by.bsuir.publisherservice.client.discussion;

import by.bsuir.publisherservice.client.discussion.mapper.DiscussionMessageMapper;
import by.bsuir.publisherservice.client.discussion.request.DiscussionMessageRequestTo;
import by.bsuir.publisherservice.client.discussion.response.DiscussionMessageResponseTo;
import by.bsuir.publisherservice.dto.message.InTopicMessage;
import by.bsuir.publisherservice.dto.message.Operation;
import by.bsuir.publisherservice.dto.message.OutTopicMessage;
import by.bsuir.publisherservice.entity.MessageState;
import by.bsuir.publisherservice.service.kafka.KafkaClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProducerService implements DiscussionServiceClient {
    private final KafkaClientService kafkaProducerService;
    private final DiscussionMessageMapper mapper;

    @Value("${spring.kafka.topic.request}")
    private String topicName;

    @Override
    public List<DiscussionMessageResponseTo> getAllMessages(Integer page, Integer size) {
        return Optional.of(kafkaProducerService.sendSyncMessage(
                        InTopicMessage.builder()
                                .operation(Operation.GET_ALL)
                                .page(page)
                                .size(size)
                                .build(),
                        topicName
                ))
                .filter(OutTopicMessage::isSuccessful)
                .map(OutTopicMessage::result)
                .orElseThrow();
    }

    @Override
    public List<DiscussionMessageResponseTo> getMessagesByStoryId(Long id, Integer page, Integer size) {
        return Optional.of(kafkaProducerService.sendSyncMessage(
                        InTopicMessage.builder()
                                .operation(Operation.GET_BY_STORY_ID)
                                .message(DiscussionMessageRequestTo.builder()
                                        .storyId(id)
                                        .build())
                                .page(page)
                                .size(size)
                                .build(),
                        topicName
                ))
                .filter(OutTopicMessage::isSuccessful)
                .map(OutTopicMessage::result)
                .orElseThrow();
    }

    @Override
    public DiscussionMessageResponseTo getMessageById(Long id) {
        return Optional.of(kafkaProducerService.sendSyncMessage(
                        InTopicMessage.builder()
                                .operation(Operation.GET_BY_ID)
                                .message(DiscussionMessageRequestTo.builder()
                                        .id(id)
                                        .build())
                                .build(),
                        topicName
                ))
                .filter(OutTopicMessage::isSuccessful)
                .map(OutTopicMessage::result)
                .map(List::getFirst)
                .orElseThrow();
    }

    @Override
    public DiscussionMessageResponseTo createMessage(DiscussionMessageRequestTo message) {
        kafkaProducerService.sendMessage(
                InTopicMessage.builder()
                        .operation(Operation.CREATE)
                        .message(message)
                        .build(),
                topicName);
        return mapper.toDiscussionResponse(message, MessageState.PENDING);
    }

    @Override
    public DiscussionMessageResponseTo updateMessage(DiscussionMessageRequestTo message) {
        return Optional.of(kafkaProducerService.sendSyncMessage(
                        InTopicMessage.builder()
                                .operation(Operation.UPDATE)
                                .message(message)
                                .build(),
                        topicName
                ))
                .map(m -> {
                    log.info("Update message: {}", m);
                    return m;
                })
                .filter(OutTopicMessage::isSuccessful)
                .map(OutTopicMessage::result)
                .map(List::getFirst)
                .orElseThrow();
    }

    @Override
    public void deleteMessage(Long id) {
        Optional.of(kafkaProducerService.sendSyncMessage(
                        InTopicMessage.builder()
                                .operation(Operation.DELETE)
                                .message(DiscussionMessageRequestTo.builder()
                                        .id(id)
                                        .build())
                                .build(),
                        topicName
                ))
                .filter(OutTopicMessage::isSuccessful)
                .orElseThrow();
    }
}
