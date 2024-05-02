package com.poluectov.reproject.discussion.service;

import com.poluectov.reproject.discussion.dto.message.MessageRequestTo;
import com.poluectov.reproject.discussion.dto.message.MessageResponseTo;
import com.poluectov.reproject.discussion.model.Message;
import com.poluectov.reproject.discussion.repository.MessageRepository;
import com.poluectov.reproject.discussion.utils.dtoconverter.DtoConverter;
import com.poluectov.reproject.discussion.utils.dtoconverter.modelassembler.MessageRequestDtoConverter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Random;

@Service
public class MessageService extends CommonRestService {

    @Autowired
    public MessageService(MessageRepository repository,
                          MessageRequestDtoConverter dtoConverter) {
        super(repository, dtoConverter);
    }

    @Override
    public Optional<MessageResponseTo> create(MessageRequestTo messageRequestTo) {
        return super.create(messageRequestTo);
    }

    Optional<MessageResponseTo> mapResponseTo(Message message) {
        return Optional.ofNullable(MessageResponseTo.builder()
                .id(message.getId())
                .issueId(message.getIssueId())
                .content(message.getContent())
                .country(message.getCountry())
                .build());
    }

    @Override
    void update(Message one, Message found) {
        one.setIssueId(found.getIssueId());
        one.setContent(found.getContent());
    }
}
