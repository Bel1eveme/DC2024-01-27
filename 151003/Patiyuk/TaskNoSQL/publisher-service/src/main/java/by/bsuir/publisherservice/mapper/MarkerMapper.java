package by.bsuir.publisherservice.mapper;

import by.bsuir.publisherservice.dto.request.MarkerRequestTo;
import by.bsuir.publisherservice.dto.response.MarkerResponseTo;
import by.bsuir.publisherservice.entity.Marker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MarkerMapper {

    @Mapping(target = "id", ignore = true)
    Marker toEntity(MarkerRequestTo request);

    MarkerResponseTo toResponseTo(Marker entity);

    Marker updateEntity(@MappingTarget Marker entity, MarkerRequestTo request);
}
