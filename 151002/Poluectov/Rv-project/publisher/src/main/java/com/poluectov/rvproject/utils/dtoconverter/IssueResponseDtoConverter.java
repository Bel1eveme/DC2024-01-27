package com.poluectov.rvproject.utils.dtoconverter;

import com.poluectov.rvproject.dto.issue.IssueResponseTo;
import com.poluectov.rvproject.dto.marker.MarkerResponseTo;
import com.poluectov.rvproject.model.Issue;
import com.poluectov.rvproject.model.Marker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
@AllArgsConstructor
public class IssueResponseDtoConverter implements DtoConverter<Issue, IssueResponseTo> {


    UserResponseDtoConverter userResponseDtoConverter;
    MarkerResponseDtoConverter markerResponseDtoConverter;
    @Override
    public IssueResponseTo convert(Issue issue) {
        List<Marker> markerList = issue.getMarkers();
        List<Long> markers = null;
        if (markerList != null) {
            markers = markerList.stream().map(Marker::getId).toList();
        }

        return IssueResponseTo.builder()
               .id(issue.getId())
               .userId(issue.getUser().getId())
               .title(issue.getTitle())
               .content(issue.getContent())
               .created(issue.getCreated())
               .modified(issue.getModified())
               .markers(markers)
               .build();
   }
}
