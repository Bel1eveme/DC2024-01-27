package com.poluectov.rvproject.service.message;

import com.poluectov.rvproject.dto.message.MessageRequestTo;
import com.poluectov.rvproject.dto.message.MessageResponseTo;
import com.poluectov.rvproject.model.Message;
import com.poluectov.rvproject.repository.MessageRepository;
import com.poluectov.rvproject.service.CommonRestService;
import com.poluectov.rvproject.utils.dtoconverter.MessageRequestDtoConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageService extends CommonRestService<Message, MessageRequestTo, MessageResponseTo, Long> {

    public MessageService(
            @Qualifier("kafkaMessageRepository") MessageRepository repository,
            MessageRequestDtoConverter messageRequestDtoConverter) {
        super(repository, messageRequestDtoConverter);
    }

    protected Optional<MessageResponseTo> mapResponseTo(Message message) {
        return Optional.ofNullable(MessageResponseTo.builder()
                .id(message.getId())
                .issueId(message.getIssueId())
                .content(message.getContent())
                .build());
    }

    @Override
    protected void update(Message one, Message found) {
        one.setIssueId(found.getIssueId());
        one.setContent(found.getContent());
    }

    @Override
    public Optional<MessageResponseTo> update(Long aLong, MessageRequestTo messageRequestTo) {
        MessageRepository messageRepository = (MessageRepository) this.crudRepository;

        messageRequestTo.setId(aLong);
        Message updated = messageRepository.update(this.dtoConverter.convert(messageRequestTo));

        return mapResponseTo(updated);
    }
}
