package by.bsuir.publisherservice.service;

import by.bsuir.publisherservice.dto.request.MarkerRequestTo;
import by.bsuir.publisherservice.dto.response.MarkerResponseTo;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface MarkerService {
    List<MarkerResponseTo> getAllMarkers(PageRequest pageRequest);
    List<MarkerResponseTo> getMarkersByStoryId(Long id, PageRequest pageRequest);
    MarkerResponseTo getMarkerById(Long id);
    MarkerResponseTo createMarker(MarkerRequestTo marker);
    MarkerResponseTo updateMarker(MarkerRequestTo marker);
    MarkerResponseTo updateMarker(Long id, MarkerRequestTo marker);
    void deleteMarker(Long id);
}
