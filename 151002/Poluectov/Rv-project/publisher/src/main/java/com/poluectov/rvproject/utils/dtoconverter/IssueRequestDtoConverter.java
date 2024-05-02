package com.poluectov.rvproject.utils.dtoconverter;

import com.poluectov.rvproject.dto.issue.IssueRequestTo;
import com.poluectov.rvproject.model.Issue;
import com.poluectov.rvproject.model.Marker;
import com.poluectov.rvproject.model.User;
import com.poluectov.rvproject.repository.MarkerRepository;
import com.poluectov.rvproject.repository.UserRepository;
import com.poluectov.rvproject.repository.exception.EntityNotFoundException;
import com.poluectov.rvproject.repository.jpa.JpaMarkerRepository;
import com.poluectov.rvproject.repository.jpa.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;


@Component
@AllArgsConstructor
public class IssueRequestDtoConverter implements DtoConverter<IssueRequestTo, Issue> {

    private JpaUserRepository userRepository;

    private JpaMarkerRepository markerRepository;

    @Override
    public Issue convert(IssueRequestTo issue) {
        if (issue == null) {
            return null;
        }
        //check if user and markers exists
        User user = userRepository
                .findById(issue.getUserId())
                .orElseThrow( () ->
                        new EntityNotFoundException(
                                "User with id " + issue.getUserId() + " not found"
                        )
                );
        List<Marker> markerList = null;
        //check if markers exists
        if (issue.getMarkers() != null) {
            markerList = markerRepository.findByIdIn(issue.getMarkers());
            //if some markers are not found
            if(markerList.size() != issue.getMarkers().size()) {
                List<Long> ids =findMissingIds(markerList, issue.getMarkers());
                throw new EntityNotFoundException("Markers with ids " + getMissingIdsMessage(ids) + " not found");
            }
        }



        return Issue.builder()
                .id(issue.getId())
                .user(user)
                .title(issue.getTitle())
                .content(issue.getContent())
                .created(issue.getCreated())
                .modified(issue.getModified())
                .markers(markerList)
                .build();
    }
    private List<Long> findMissingIds(List<Marker> markerList, List<Long> idList) {
        List<Long> allMarkerIds = markerList.stream()
                .map(Marker::getId)
                .toList();

        // Фильтруем список idList, оставляя только те, которых нет в allMarkerIds
        return idList.stream()
                .filter(id -> !allMarkerIds.contains(id))
                .collect(Collectors.toList());
    }

    private String getMissingIdsMessage(List<Long> ids) {
        return ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}
