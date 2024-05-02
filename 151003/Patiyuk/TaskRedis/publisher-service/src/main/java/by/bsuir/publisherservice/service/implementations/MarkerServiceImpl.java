package by.bsuir.publisherservice.service.implementations;

import by.bsuir.publisherservice.dto.request.MarkerRequestTo;
import by.bsuir.publisherservice.dto.response.MarkerResponseTo;
import by.bsuir.publisherservice.exception.CreateEntityException;
import by.bsuir.publisherservice.exception.EntityNotFoundException;
import by.bsuir.publisherservice.mapper.MarkerMapper;
import by.bsuir.publisherservice.repository.MarkerRepository;
import by.bsuir.publisherservice.service.MarkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MarkerServiceImpl implements MarkerService {

    private final MarkerRepository markerRepository;
    private final MarkerMapper mapper;

    @Override
    @Cacheable(value = "markers")
    public List<MarkerResponseTo> getAllMarkers(PageRequest pageRequest) {
        return new ArrayList<>(markerRepository.findAll(pageRequest)
                .stream()
                .map(mapper::toResponseTo)
                .toList());
    }

    @Override
    public List<MarkerResponseTo> getMarkersByStoryId(Long id, PageRequest pageRequest) {
        return markerRepository.findByStories_Id(id, pageRequest)
                .stream()
                .map(mapper::toResponseTo)
                .toList();
    }

    @Override
    @Cacheable(value = "markers", key = "#id")
    public MarkerResponseTo getMarkerById(Long id) {
        return markerRepository.findById(id)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new EntityNotFoundException("Marker with id " + id + " not found"));
    }

    @Override
    @CacheEvict(cacheNames = "markers", allEntries = true)
    public MarkerResponseTo createMarker(MarkerRequestTo marker) {
        return Optional.of(marker)
                .map(mapper::toEntity)
                .map(markerRepository::save)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new CreateEntityException("Marker with id " + marker.id() + " not created"));
    }

    @Override
    @CacheEvict(cacheNames = "markers", allEntries = true)
    public MarkerResponseTo updateMarker(MarkerRequestTo marker) {
        return updateMarker(marker.id(), marker);
    }

    @Override
    @CacheEvict(cacheNames = "markers", allEntries = true)
    public MarkerResponseTo updateMarker(Long id, MarkerRequestTo marker) {
        return markerRepository.findById(id)
                .map(entity -> mapper.updateEntity(entity, marker))
                .map(markerRepository::save)
                .map(mapper::toResponseTo)
                .orElseThrow(()
                        -> new EntityNotFoundException("Marker with id " + id + " not found"));
    }

    @Override
    @CacheEvict(cacheNames = "markers", allEntries = true)
    public void deleteMarker(Long id) {
        markerRepository.findById(id)
                .ifPresentOrElse(markerRepository::delete, () -> {
                    throw new EntityNotFoundException("Marker with id " + id + " not found");
                });
    }
}
