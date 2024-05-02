package by.bsuir.publisher.services.impl;

import by.bsuir.publisher.domain.Issue;
import by.bsuir.publisher.dto.requests.IssueRequestDto;
import by.bsuir.publisher.dto.requests.converters.IssueRequestConverter;
import by.bsuir.publisher.dto.responses.IssueResponseDto;
import by.bsuir.publisher.dto.responses.converters.CollectionIssueResponseConverter;
import by.bsuir.publisher.dto.responses.converters.IssueResponseConverter;
import by.bsuir.publisher.exceptions.EntityExistsException;
import by.bsuir.publisher.exceptions.Messages;
import by.bsuir.publisher.exceptions.NoEntityExistsException;
import by.bsuir.publisher.repositories.IssueRepository;
import by.bsuir.publisher.services.IssueService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final IssueRequestConverter issueRequestConverter;
    private final IssueResponseConverter issueResponseConverter;
    private final CollectionIssueResponseConverter collectionIssueResponseConverter;

    @Override
    @Validated
    public IssueResponseDto create(@Valid @NonNull IssueRequestDto dto) throws EntityExistsException {
        Optional<Issue> issue = dto.getId() == null ? Optional.empty() : issueRepository.findById(dto.getId());
        if (issue.isEmpty()) {
            return issueResponseConverter.toDto(issueRepository.save(issueRequestConverter.fromDto(dto)));
        } else {
            throw new EntityExistsException(Messages.EntityExistsException);
        }
    }

    @Override
    public Optional<IssueResponseDto> read(@NonNull Long id) {
        return issueRepository.findById(id).flatMap(issue -> Optional.of(
                issueResponseConverter.toDto(issue)));
    }

    @Override
    @Validated
    public IssueResponseDto update(@Valid @NonNull IssueRequestDto dto) throws NoEntityExistsException {
        Optional<Issue> issue = dto.getId() == null || issueRepository.findById(dto.getId()).isEmpty() ?
                Optional.empty() : Optional.of(issueRequestConverter.fromDto(dto));
        return issueResponseConverter.toDto(issueRepository.save(issue.orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException))));
    }

    @Override
    public Long delete(@NonNull Long id) throws NoEntityExistsException {
        Optional<Issue> issue = issueRepository.findById(id);
        issueRepository.deleteById(issue.map(Issue::getId).orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException)));
        return issue.get().getId();
    }

    @Override
    public List<IssueResponseDto> readAll() {
        return collectionIssueResponseConverter.toListDto(issueRepository.findAll());
    }
}
