package com.example.dc_project.service;

import com.example.dc_project.dao.repository.MessageRepository;
import com.example.dc_project.model.request.MessageRequestTo;
import com.example.dc_project.model.response.MessageResponseTo;
import com.example.dc_project.service.exceptions.ResourceNotFoundException;
import com.example.dc_project.service.exceptions.ResourceStateException;
import com.example.dc_project.service.mapper.MessageMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class MessageService implements IService<MessageRequestTo, MessageResponseTo>{
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public MessageResponseTo findById(Long id) {
        return messageRepository.getById(id).map(messageMapper::getResponse).orElseThrow(() -> findByIdException(id));
    }

    @Override
    public List<MessageResponseTo> findAll() {
        return messageMapper.getListResponse(messageRepository.getAll());
    }

    @Override
    public MessageResponseTo create(MessageRequestTo request) {
        return messageRepository.save(messageMapper.getMessage(request)).map(messageMapper::getResponse).orElseThrow(MessageService::createException);
    }

    @Override
    public MessageResponseTo update(MessageRequestTo request) {
        if (messageMapper.getMessage(request).getId() == null)
        {
            throw findByIdException(messageMapper.getMessage(request).getId());
        }

        return messageRepository.update(messageMapper.getMessage(request)).map(messageMapper::getResponse).orElseThrow(MessageService::updateException);
    }

    @Override
    public boolean removeById(Long id) {
        if (!messageRepository.removeById(id)) {
            throw removeException();
        }
        return true;
    }

    private static ResourceNotFoundException findByIdException(Long id) {
        return new ResourceNotFoundException(HttpStatus.NOT_FOUND.value() * 100 + 31, "Can't find message by id = " + id);
    }

    private static ResourceStateException createException() {
        return new ResourceStateException(HttpStatus.CONFLICT.value() * 100 + 32, "Can't create message");
    }

    private static ResourceStateException updateException() {
        return new ResourceStateException(HttpStatus.CONFLICT.value() * 100 + 33, "Can't update message");
    }

    private static ResourceStateException removeException() {
        return new ResourceStateException(HttpStatus.CONFLICT.value() * 100 + 34, "Can't remove message");
    }
}