package com.poluectov.rvproject.utils.dtoconverter;

import com.poluectov.rvproject.dto.marker.MarkerResponseTo;
import com.poluectov.rvproject.model.Marker;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class MarkerResponseDtoConverter implements DtoConverter<Marker, MarkerResponseTo> {

    @Override
    public MarkerResponseTo convert(Marker marker) {
        return MarkerResponseTo.builder()
                .id(marker.getId())
                .name(marker.getName())
                .build();
    }
}
