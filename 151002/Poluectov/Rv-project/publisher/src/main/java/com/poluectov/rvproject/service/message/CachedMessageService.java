package com.poluectov.rvproject.service.message;

import com.poluectov.rvproject.dto.message.MessageRequestTo;
import com.poluectov.rvproject.dto.message.MessageResponseTo;
import com.poluectov.rvproject.model.Message;
import com.poluectov.rvproject.repository.MessageRepository;
import com.poluectov.rvproject.repository.exception.EntityNotFoundException;
import com.poluectov.rvproject.service.redis.RedisCacheService;
import com.poluectov.rvproject.utils.dtoconverter.MessageRequestDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class CachedMessageService extends MessageService {

    RedisCacheService<Long, MessageResponseTo> redisCacheService;
    ExecutorService executorService;

    @Autowired
    public CachedMessageService(
            @Qualifier("kafkaMessageRepository") MessageRepository repository,
            MessageRequestDtoConverter messageRequestDtoConverter,
            RedisCacheService<Long, MessageResponseTo> redisCacheService,
            @Value("${redis.threads.count}") Integer threadsCount
    ) {
        super(repository, messageRequestDtoConverter);
        this.redisCacheService = redisCacheService;
        this.executorService = Executors.newFixedThreadPool(threadsCount);
    }

    @Override
    public Optional<MessageResponseTo> update(Long aLong, MessageRequestTo messageRequestTo) {

        if (redisCacheService.get(aLong) != null) {
            // update async
            executorService.execute(() -> {
                redisCacheService.put(aLong, convert(messageRequestTo));
                super.update(aLong, messageRequestTo);
            });

            return Optional.of(convert(messageRequestTo));
        }

        Optional<MessageResponseTo> messageResponseTo = super.update(aLong, messageRequestTo);
        messageResponseTo.ifPresent(responseTo -> redisCacheService.put(aLong, responseTo));
        return messageResponseTo;
    }

    @Override
    public void delete(Long aLong) throws EntityNotFoundException {
        redisCacheService.delete(aLong);
        super.delete(aLong);
    }

    @Override
    public Optional<MessageResponseTo> one(Long aLong) {
        MessageResponseTo messageResponseTo = redisCacheService.get(aLong);
        if (messageResponseTo != null) {
            return Optional.of(messageResponseTo);
        }

        Optional<MessageResponseTo> response = super.one(aLong);
        response.ifPresent(messageResponseTo1 -> redisCacheService.put(aLong, messageResponseTo1));
        return response;
    }

    @Override
    public Optional<MessageResponseTo> create(MessageRequestTo messageRequestTo) {
        Optional<MessageResponseTo> messageResponseTo = super.create(messageRequestTo);
        messageResponseTo.ifPresent(responseTo -> redisCacheService.put(responseTo.getId(), responseTo));

        return messageResponseTo;
    }

    private MessageResponseTo convert(MessageRequestTo messageRequestTo) {
        return MessageResponseTo.builder()
                .id(messageRequestTo.getId())
                .content(messageRequestTo.getContent())
                .issueId(messageRequestTo.getIssueId())
                .build();
    }
}
