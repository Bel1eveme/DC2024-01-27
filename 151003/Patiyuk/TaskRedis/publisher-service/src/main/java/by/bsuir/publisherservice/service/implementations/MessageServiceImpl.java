package by.bsuir.publisherservice.service.implementations;

import by.bsuir.publisherservice.client.discussion.DiscussionServiceClient;
import by.bsuir.publisherservice.client.discussion.mapper.DiscussionMessageMapper;
import by.bsuir.publisherservice.dto.request.MessageRequestTo;
import by.bsuir.publisherservice.dto.response.MessageResponseTo;
import by.bsuir.publisherservice.exception.CreateEntityException;
import by.bsuir.publisherservice.repository.StoryRepository;
import by.bsuir.publisherservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final StoryRepository storyRepository;
    private final DiscussionServiceClient discussionServiceClient;
    private final DiscussionMessageMapper discussionMessageMapper;

    @Override
    @Cacheable(value = "messages")
    public List<MessageResponseTo> getAllMessages(PageRequest pageRequest) {
        return new ArrayList<>(discussionServiceClient.getAllMessages(pageRequest.getPageNumber(), pageRequest.getPageSize())
                .stream()
                .map(discussionMessageMapper::toMessageResponse)
                .toList());
    }

    @Override
    public List<MessageResponseTo> getMessagesByStoryId(Long id, PageRequest pageRequest) {
        return discussionServiceClient.getMessagesByStoryId(id, pageRequest.getPageNumber(), pageRequest.getPageSize())
                .stream()
                .map(discussionMessageMapper::toMessageResponse)
                .toList();
    }

    @Override
    @Cacheable(value = "messages", key = "#id")
    public MessageResponseTo getMessageById(Long id) {
        return discussionMessageMapper.toMessageResponse(discussionServiceClient.getMessageById(id));
    }

    @Override
    @CacheEvict(value = "messages", allEntries = true)
    public MessageResponseTo createMessage(MessageRequestTo message, String country) {
        return storyRepository.findById(message.storyId())
                .map(story -> discussionMessageMapper.toDiscussionRequest(message, createId(), country))
                .map(discussionServiceClient::createMessage)
                .map(discussionMessageMapper::toMessageResponse)
                .orElseThrow(() -> new CreateEntityException("Story with id " + message.storyId() + " not found"));
    }

    @Override
    @CacheEvict(value = "messages", allEntries = true)
    public MessageResponseTo updateMessage(MessageRequestTo message, String country) {
        return updateMessage(message.id(), message, country);
    }

    @Override
    @CacheEvict(value = "messages", allEntries = true)
    public MessageResponseTo updateMessage(Long id, MessageRequestTo message, String country) {
        return storyRepository.findById(message.storyId())
                .map(story -> discussionMessageMapper.toDiscussionRequest(message, country))
                .map(discussionServiceClient::updateMessage)
                .map(discussionMessageMapper::toMessageResponse)
                .orElseThrow(() -> new CreateEntityException("Story with id " + message.storyId() + " not found"));
    }

    @Override
    @CacheEvict(value = "messages", allEntries = true)
    public void deleteMessage(Long id) {
        discussionServiceClient.deleteMessage(id);
    }

    private static Long createId() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}
