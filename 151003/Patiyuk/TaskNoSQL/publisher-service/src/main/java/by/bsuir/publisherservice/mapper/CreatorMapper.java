package by.bsuir.publisherservice.mapper;

import by.bsuir.publisherservice.dto.request.CreatorRequestTo;
import by.bsuir.publisherservice.dto.response.CreatorResponseTo;
import by.bsuir.publisherservice.entity.Creator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CreatorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", source = "firstname")
    @Mapping(target = "lastName", source = "lastname")
    Creator toEntity(CreatorRequestTo request);

    @Mapping(target = "firstname", source = "firstName")
    @Mapping(target = "lastname", source = "lastName")
    CreatorResponseTo toResponseTo(Creator entity);

    @Mapping(target = "firstName", source = "firstname")
    @Mapping(target = "lastName", source = "lastname")
    Creator updateEntity(@MappingTarget Creator entity, CreatorRequestTo request);
}
