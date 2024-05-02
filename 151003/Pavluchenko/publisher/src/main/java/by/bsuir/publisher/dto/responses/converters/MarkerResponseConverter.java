package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Marker;
import by.bsuir.publisher.dto.responses.MarkerResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarkerResponseConverter {
    MarkerResponseDto toDto(Marker marker);
}
