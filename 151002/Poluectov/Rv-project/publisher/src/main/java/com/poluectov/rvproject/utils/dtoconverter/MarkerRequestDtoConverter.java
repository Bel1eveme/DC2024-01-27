package com.poluectov.rvproject.utils.dtoconverter;

import com.poluectov.rvproject.dto.marker.MarkerRequestTo;
import com.poluectov.rvproject.model.Marker;
import org.springframework.stereotype.Component;

@Component
public class MarkerRequestDtoConverter implements DtoConverter<MarkerRequestTo, Marker> {

    @Override
    public Marker convert(MarkerRequestTo marker) {
        return Marker.builder()
                .id(marker.getId())
                .name(marker.getName())
                .build();
    }
}
