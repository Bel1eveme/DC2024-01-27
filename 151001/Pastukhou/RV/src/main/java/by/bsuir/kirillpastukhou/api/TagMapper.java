package by.bsuir.kirillpastukhou.api;

import by.bsuir.kirillpastukhou.impl.bean.Tag;
import by.bsuir.kirillpastukhou.impl.dto.TagRequestTo;
import by.bsuir.kirillpastukhou.impl.dto.TagResponseTo;
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
