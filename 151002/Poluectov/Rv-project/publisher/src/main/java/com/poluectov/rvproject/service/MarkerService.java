package com.poluectov.rvproject.service;

import com.poluectov.rvproject.dto.marker.MarkerRequestTo;
import com.poluectov.rvproject.dto.marker.MarkerResponseTo;
import com.poluectov.rvproject.model.Marker;
import com.poluectov.rvproject.repository.MarkerRepository;
import com.poluectov.rvproject.repository.jpa.JpaMarkerRepository;
import com.poluectov.rvproject.utils.dtoconverter.MarkerRequestDtoConverter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Optional;

@Component
public class MarkerService extends CommonRestService<Marker, MarkerRequestTo, MarkerResponseTo, Long> {
    public MarkerService(
            @Qualifier("customJpaMarkerRepository") MarkerRepository repository,
            MarkerRequestDtoConverter dtoConverter) {
        super(repository, dtoConverter);
    }

    @Override
    protected Optional<MarkerResponseTo> mapResponseTo(Marker marker) {
        if (marker == null) {
            return Optional.empty();
        }
        return Optional.of(MarkerResponseTo.builder()
                .id(marker.getId())
                .name(marker.getName())
                .build());
    }

    @Override
    protected void update(Marker one, Marker found) {
        one.setName(found.getName());
    }
}
