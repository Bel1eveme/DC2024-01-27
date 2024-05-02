package com.example.dc_project.service;

import com.example.dc_project.dao.repository.LabelRepository;
import com.example.dc_project.model.request.LabelRequestTo;
import com.example.dc_project.model.response.LabelResponseTo;
import com.example.dc_project.service.exceptions.ResourceStateException;
import com.example.dc_project.service.exceptions.ResourceNotFoundException;
import com.example.dc_project.service.mapper.LabelMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class LabelService implements IService<LabelRequestTo, LabelResponseTo> {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    @Override
    public LabelResponseTo findById(Long id) {
        return labelRepository.getById(id).map(labelMapper::getResponse).orElseThrow(() -> findByIdException(id));
    }

    @Override
    public List<LabelResponseTo> findAll() {
        return labelMapper.getListResponse(labelRepository.getAll());
    }

    @Override
    public LabelResponseTo create(LabelRequestTo request) {
        return labelRepository.save(labelMapper.getLabel(request)).map(labelMapper::getResponse).orElseThrow(LabelService::createException);
    }

    @Override
    public LabelResponseTo update(LabelRequestTo request) {
        if (labelMapper.getLabel(request).getId() == null)
        {
            throw findByIdException(labelMapper.getLabel(request).getId());
        }

        return labelRepository.update(labelMapper.getLabel(request)).map(labelMapper::getResponse).orElseThrow(LabelService::updateException);
    }

    @Override
    public boolean removeById(Long id) {
        if (!labelRepository.removeById(id)) {
            throw removeException();
        }
        return true;
    }

    private static ResourceNotFoundException findByIdException(Long id) {
        return new ResourceNotFoundException(HttpStatus.NOT_FOUND.value() * 100 + 21, "Can't find label by id = " + id);
    }

    private static ResourceStateException createException() {
        return new ResourceStateException(HttpStatus.CONFLICT.value() * 100 + 22, "Can't create label");
    }

    private static ResourceStateException updateException() {
        return new ResourceStateException(HttpStatus.CONFLICT.value() * 100 + 23, "Can't update label");
    }

    private static ResourceStateException removeException() {
        return new ResourceStateException(HttpStatus.CONFLICT.value() * 100 + 24, "Can't remove label");
    }
}
