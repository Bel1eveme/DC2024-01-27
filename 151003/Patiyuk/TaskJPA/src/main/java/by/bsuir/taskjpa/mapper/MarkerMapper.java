package by.bsuir.taskrest.mapper;

import by.bsuir.taskrest.dto.request.MarkerRequestTo;
import by.bsuir.taskrest.dto.response.MarkerResponseTo;
import by.bsuir.taskrest.entity.Marker;
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
