package com.example.dc_project.service;

import com.example.dc_project.dao.repository.IssueRepository;
import com.example.dc_project.model.request.IssueRequestTo;
import com.example.dc_project.model.response.IssueResponseTo;
import com.example.dc_project.service.exceptions.ResourceNotFoundException;
import com.example.dc_project.service.exceptions.ResourceStateException;
import com.example.dc_project.service.mapper.IssueMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class IssueService implements IService<IssueRequestTo, IssueResponseTo> {
    private final IssueRepository issueRepository;
    private final IssueMapper issueMapper;

    @Override
    public IssueResponseTo findById(Long id) {
        return issueRepository.getById(id).map(issueMapper::getResponse).orElseThrow(() -> findByIdException(id));
    }

    @Override
    public List<IssueResponseTo> findAll() {
        return issueMapper.getListResponse(issueRepository.getAll());
    }

    @Override
    public IssueResponseTo create(IssueRequestTo request) {
        return issueRepository.save(issueMapper.getIssue(request)).map(issueMapper::getResponse).orElseThrow(IssueService::createException);
    }

    @Override
    public IssueResponseTo update(IssueRequestTo request) {
        if (issueMapper.getIssue(request).getId() == null)
        {
            throw findByIdException(issueMapper.getIssue(request).getId());
        }

        return issueRepository.update(issueMapper.getIssue(request)).map(issueMapper::getResponse).orElseThrow(IssueService::updateException);
    }

    @Override
    public boolean removeById(Long id) {
        if (!issueRepository.removeById(id)) {
            throw removeException();
        }
        return true;
    }

    private static ResourceNotFoundException findByIdException(Long id) {
        return new ResourceNotFoundException(HttpStatus.NOT_FOUND.value() * 100 + 41, "Can't find story by id = " + id);
    }

    private static ResourceStateException createException() {
        return new ResourceStateException(HttpStatus.CONFLICT.value() * 100 + 42, "Can't create story");
    }

    private static ResourceStateException updateException() {
        return new ResourceStateException(HttpStatus.CONFLICT.value() * 100 + 43, "Can't update story");
    }

    private static ResourceStateException removeException() {
        return new ResourceStateException(HttpStatus.CONFLICT.value() * 100 + 44, "Can't remove story");
    }
}
