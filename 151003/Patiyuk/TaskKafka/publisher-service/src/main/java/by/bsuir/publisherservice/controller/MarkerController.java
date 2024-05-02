package by.bsuir.publisherservice.controller;

import by.bsuir.publisherservice.dto.request.MarkerRequestTo;
import by.bsuir.publisherservice.dto.response.MarkerResponseTo;
import by.bsuir.publisherservice.service.MarkerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/markers")
public class MarkerController {

    private final MarkerService markerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MarkerResponseTo> getAllMarkers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return markerService.getAllMarkers(PageRequest.of(page, size));
    }

    @GetMapping("/story/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<MarkerResponseTo> getMarkersByStoryId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return markerService.getMarkersByStoryId(id, PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MarkerResponseTo getMarkerById(@PathVariable Long id) {
        return markerService.getMarkerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MarkerResponseTo createMarker(@Valid @RequestBody MarkerRequestTo marker) {
        return markerService.createMarker(marker);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public MarkerResponseTo updateMarker(@Valid @RequestBody MarkerRequestTo marker) {
        return markerService.updateMarker(marker);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MarkerResponseTo updateMarker(@PathVariable Long id, @Valid @RequestBody MarkerRequestTo marker) {
        return markerService.updateMarker(id, marker);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMarker(@PathVariable Long id) {
        markerService.deleteMarker(id);
    }
}
