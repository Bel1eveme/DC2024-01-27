package com.poluectov.reproject.discussion.service;

import com.poluectov.reproject.discussion.dto.message.MessageRequestTo;
import com.poluectov.reproject.discussion.dto.message.MessageResponseTo;
import com.poluectov.reproject.discussion.model.IdentifiedEntity;
import com.poluectov.reproject.discussion.model.Message;
import com.poluectov.reproject.discussion.model.MessageKey;
import com.poluectov.reproject.discussion.repository.MessageRepository;
import com.poluectov.reproject.discussion.repository.exception.EntityNotFoundException;
import com.poluectov.reproject.discussion.utils.dtoconverter.DtoConverter;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Validated
public abstract class CommonRestService {
    MessageRepository repository;
    DtoConverter<MessageRequestTo, Message> dtoConverter;

    public CommonRestService(MessageRepository repository,
                             DtoConverter<MessageRequestTo, Message> dtoConverter) {
        this.repository = repository;
        this.dtoConverter = dtoConverter;
    }

    abstract Optional<MessageResponseTo> mapResponseTo(Message entity);

    abstract void update(Message one, Message found);

    public List<MessageResponseTo> all() {
        List<MessageResponseTo> all = new ArrayList<>();
        for (Message one : repository.findAll()) {
            Optional<MessageResponseTo> to = mapResponseTo(one);
            to.ifPresent(all::add);
        }
        return all;
    }

    public Optional<MessageResponseTo> one(Long id) {


        List<Message> one = repository.findById(id);
        if (!one.isEmpty()) {
            return mapResponseTo(one.get(0));
        }
        return Optional.empty();
    }

    public Optional<MessageResponseTo> create(@Valid MessageRequestTo request) {
        Message one = dtoConverter.convert(request);
        Message entity = repository.save(one);
        return mapResponseTo(entity);
    }

    @Transactional
    public Optional<MessageResponseTo> update(Long id, @Valid MessageRequestTo request) {
        //repository updates by id
        Message newUser = dtoConverter.convert(request);

        List<Message> found = repository.findById(id);

        if (found.isEmpty()) {
            throw new EntityNotFoundException(getEntityName() + " with id " + id + " not found");
        }


        Message one = found.get(0);
        repository.deleteById(one.getCountry(), one.getIssueId(), one.getId());
        update(one, newUser);

        Message entity = repository.save(one);
        return mapResponseTo(entity);
    }
    @Transactional
    public void delete(Long id) throws EntityNotFoundException {
        try{
            List<Message> toDeleteList = repository.findById(id);

            Message toDelete = toDeleteList.get(0);
            repository.deleteById(toDelete.getCountry(), toDelete.getIssueId(), toDelete.getId());
        }catch (Exception e){
            throw new EntityNotFoundException(getEntityName() + " with id " + id + " not found");
        }
    }

    protected String getEntityName(){
        String className = this.getClass().getSimpleName();
        className = className.replace("Service", "");
        for(int i = className.length() - 1; i >= 0; i--){
            if(Character.isUpperCase(className.charAt(i))){
                className = className.substring(i);
                break;
            }
        }
        return className;
    }

}
