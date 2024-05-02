package by.bsuir.discussionservice.service.implementations;

import by.bsuir.discussionservice.dto.request.MessageRequestTo;
import by.bsuir.discussionservice.dto.response.MessageResponseTo;
import by.bsuir.discussionservice.entity.MessageState;
import by.bsuir.discussionservice.exception.CreateEntityException;
import by.bsuir.discussionservice.exception.EntityNotFoundException;
import by.bsuir.discussionservice.mapper.MessageMapper;
import by.bsuir.discussionservice.repository.MessageRepository;
import by.bsuir.discussionservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
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
        return messageRepository.findAllByKey_StoryId(id, pageRequest)
                .stream()
                .map(mapper::toResponseTo)
                .toList();
    }

    @Override
    public MessageResponseTo getMessageById(Long id) {
        return messageRepository.findByKey_Id(id)
                .map(mapper::toResponseTo)
                .orElseThrow(() -> new EntityNotFoundException("Message with id " + id + " not found"));
    }

    @Override
    public MessageResponseTo createMessage(MessageRequestTo message) {
        return Optional.of(message)
                .filter(m -> !messageRepository.existsByKey_Id(m.id()))
                .map(mapper::toEntity)
                .map(messageRepository::save)
                .map(m -> mapper.toResponseTo(m, moderateMessage(message)))
                .orElseThrow(() -> new CreateEntityException("Message with id " + message.id() + " already exists"));
    }

    private MessageState moderateMessage(MessageRequestTo message) {
        return message.content().contains("bad word") ? MessageState.DECLINED : MessageState.APPROVED;
    }

    @Override
    public MessageResponseTo updateMessage(MessageRequestTo message) {
        return updateMessage(message.id(), message);
    }

    @Override
    public MessageResponseTo updateMessage(Long id, MessageRequestTo message) {
        return messageRepository.findByKey_Id(id)
                .map(messageEntity -> mapper.updateEntity(messageEntity, message))
                .map(messageRepository::save)
                .map(mapper::toResponseTo)
                .orElseThrow(() -> new CreateEntityException("Message with id " + id + " not updated"));
    }

    @Override
    public void deleteMessage(Long id) {
        messageRepository.findByKey_Id(id)
                .ifPresent(messageRepository::delete);
    }
}
