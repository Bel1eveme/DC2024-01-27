package by.bsuir.publisher.dto.requests.converters;

import by.bsuir.publisher.domain.Marker;
import by.bsuir.publisher.dto.requests.MarkerRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarkerRequestConverter {
    Marker fromDto(MarkerRequestDto marker);
}
