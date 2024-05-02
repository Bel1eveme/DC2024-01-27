package by.bsuir.taskrest.service.implementations;

import by.bsuir.taskrest.dto.request.MessageRequestTo;
import by.bsuir.taskrest.dto.response.MessageResponseTo;
import by.bsuir.taskrest.exception.CreateEntityException;
import by.bsuir.taskrest.exception.EntityNotFoundException;
import by.bsuir.taskrest.mapper.MessageMapper;
import by.bsuir.taskrest.repository.MessageRepository;
import by.bsuir.taskrest.repository.StoryRepository;
import by.bsuir.taskrest.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final StoryRepository storyRepository;
    private final MessageMapper mapper;

    @Override
    public List<MessageResponseTo> getAllMessages(PageRequest pageRequest) {
        return messageRepository.findAll(pageRequest)
                .stream()
                .map(mapper::toResponseTo)
                .toList();
    }

    @Override
    public List<MessageResponseTo> getMessagesByStoryId(Long id, PageRequest pageRequest) {
        return messageRepository.findByStoryId(id)
                .stream()
                .map(mapper::toResponseTo)
                .toList();
    }

    @Override
    public MessageResponseTo getMessageById(Long id) {
        return messageRepository.findById(id)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new EntityNotFoundException("Message with id " + id + " not found"));
    }

    @Override
    public MessageResponseTo createMessage(MessageRequestTo message) {
        return Optional.of(message)
                .map(request -> mapper.toEntity(request, storyRepository.findById(message.storyId())
                        .orElseThrow(() -> new EntityNotFoundException("Story with id " + message.storyId() + " not found"))))
                .map(messageRepository::save)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new CreateEntityException("Message with id " + message.id() + " not created"));
    }

    @Override
    public MessageResponseTo updateMessage(MessageRequestTo message) {
        return messageRepository.findById(message.id())
                .map(entity -> mapper.updateEntity(entity, message))
                .map(messageRepository::save)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new EntityNotFoundException("Message with id " + message.id() + " not found"));
    }

    @Override
    public MessageResponseTo updateMessage(Long id, MessageRequestTo message) {
        return messageRepository.findById(id)
                .map(entity -> mapper.updateEntity(entity, message))
                .map(messageRepository::save)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new EntityNotFoundException("Message with id " + id + " not found"));
    }

    @Override
    public void deleteMessage(Long id) {
        messageRepository.findById(id)
                .ifPresentOrElse(messageRepository::delete, () -> {
                    throw new EntityNotFoundException("Message with id " + id + " not found");
                });
    }
}
