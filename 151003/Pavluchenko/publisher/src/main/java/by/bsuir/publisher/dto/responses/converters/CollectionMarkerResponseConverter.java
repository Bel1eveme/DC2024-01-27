package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Marker;
import by.bsuir.publisher.dto.responses.MarkerResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = MarkerResponseConverter.class)
public interface CollectionMarkerResponseConverter {
    List<MarkerResponseDto> toListDto(List<Marker> markers);
}
