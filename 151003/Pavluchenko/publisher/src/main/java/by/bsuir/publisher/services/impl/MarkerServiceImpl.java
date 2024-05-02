package by.bsuir.publisher.services.impl;

import by.bsuir.publisher.domain.Marker;
import by.bsuir.publisher.dto.requests.MarkerRequestDto;
import by.bsuir.publisher.dto.requests.converters.MarkerRequestConverter;
import by.bsuir.publisher.dto.responses.MarkerResponseDto;
import by.bsuir.publisher.dto.responses.converters.CollectionMarkerResponseConverter;
import by.bsuir.publisher.dto.responses.converters.MarkerResponseConverter;
import by.bsuir.publisher.exceptions.EntityExistsException;
import by.bsuir.publisher.exceptions.Messages;
import by.bsuir.publisher.exceptions.NoEntityExistsException;
import by.bsuir.publisher.repositories.MarkerRepository;
import by.bsuir.publisher.services.MarkerService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Transactional(rollbackFor = {EntityExistsException.class, NoEntityExistsException.class})
public class MarkerServiceImpl implements MarkerService {

    private final MarkerRepository markerRepository;
    private final MarkerRequestConverter markerRequestConverter;
    private final MarkerResponseConverter markerResponseConverter;
    private final CollectionMarkerResponseConverter collectionMarkerResponseConverter;

    @Override
    @Validated
    public MarkerResponseDto create(@Valid @NonNull MarkerRequestDto dto) throws EntityExistsException {
        Optional<Marker> marker = dto.getId() == null ? Optional.empty() : markerRepository.findById(dto.getId());
        if (marker.isEmpty()) {
            return markerResponseConverter.toDto(markerRepository.save(markerRequestConverter.fromDto(dto)));
        } else {
            throw new EntityExistsException(Messages.EntityExistsException);
        }
    }

    @Override
    public Optional<MarkerResponseDto> read(@NonNull Long id) {
        return markerRepository.findById(id).flatMap(label -> Optional.of(
                markerResponseConverter.toDto(label)));
    }

    @Override
    @Validated
    public MarkerResponseDto update(@Valid @NonNull MarkerRequestDto dto) throws NoEntityExistsException {
        Optional<Marker> marker = dto.getId() == null || markerRepository.findById(dto.getId()).isEmpty() ?
                Optional.empty() : Optional.of(markerRequestConverter.fromDto(dto));
        return markerResponseConverter.toDto(markerRepository.save(marker.orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException))));
    }

    @Override
    public Long delete(@NonNull Long id) throws NoEntityExistsException {
        Optional<Marker> marker = markerRepository.findById(id);
        markerRepository.deleteById(marker.map(Marker::getId).orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException)));
        return marker.get().getId();
    }

    @Override
    public List<MarkerResponseDto> readAll() {
        return collectionMarkerResponseConverter.toListDto(markerRepository.findAll());
    }
}
