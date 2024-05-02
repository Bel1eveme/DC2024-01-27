package by.bsuir.nastassiayankova.Dto;

import by.bsuir.nastassiayankova.Entity.Tag;
import by.bsuir.nastassiayankova.Dto.impl.TagRequestTo;
import by.bsuir.nastassiayankova.Dto.impl.TagResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    Tag TagResponseToToTag(TagResponseTo responseTo);

    Tag TagRequestToToTag(TagRequestTo requestTo);

    TagRequestTo TagToTagRequestTo(Tag tag);

    TagResponseTo TagToTagResponseTo(Tag tag);
}
