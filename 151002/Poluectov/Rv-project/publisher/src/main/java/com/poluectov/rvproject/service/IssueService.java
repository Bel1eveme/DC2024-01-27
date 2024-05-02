package com.poluectov.rvproject.service;

import com.poluectov.rvproject.dto.issue.IssueRequestTo;
import com.poluectov.rvproject.dto.issue.IssueResponseTo;
import com.poluectov.rvproject.dto.marker.MarkerResponseTo;
import com.poluectov.rvproject.model.Issue;
import com.poluectov.rvproject.model.Marker;
import com.poluectov.rvproject.repository.IssueRepository;
import com.poluectov.rvproject.repository.jpa.JpaIssueRepository;
import com.poluectov.rvproject.utils.dtoconverter.IssueRequestDtoConverter;
import com.poluectov.rvproject.utils.dtoconverter.MarkerResponseDtoConverter;
import com.poluectov.rvproject.utils.dtoconverter.UserResponseDtoConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Component
public class IssueService extends CommonRestService<Issue, IssueRequestTo, IssueResponseTo, Long> {

    public IssueService(IssueRepository repository,
                        IssueRequestDtoConverter dtoConverter) {
        super(repository, dtoConverter);
    }

    @Override
    protected Optional<IssueResponseTo> mapResponseTo(Issue message) {
        List<Marker> markerList = message.getMarkers();
        List<Long> markers = null;
        if (markerList != null) {
            markers = markerList.stream().map(Marker::getId).toList();
        }
        return Optional.ofNullable(IssueResponseTo.builder()
                .id(message.getId())
                .title(message.getTitle())
                .userId(message.getUser().getId())
                .content(message.getContent())
                .created(message.getCreated())
                .modified(message.getModified())
                .markers(markers)
                .build());
    }

    @Override
    protected void update(Issue one, Issue found) {
        one.setUser(found.getUser());

        one.setTitle(found.getTitle());

        one.setContent(found.getContent());
        one.setCreated(found.getCreated());
        one.setModified(found.getModified());

        one.setMarkers(found.getMarkers());
    }
}
