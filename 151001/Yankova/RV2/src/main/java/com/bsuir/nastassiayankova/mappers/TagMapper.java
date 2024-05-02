package com.bsuir.nastassiayankova.mappers;

import com.bsuir.nastassiayankova.beans.entity.Tag;
import com.bsuir.nastassiayankova.beans.request.TagRequestTo;
import com.bsuir.nastassiayankova.beans.response.TagResponseTo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = NewsListMapper.class)
public interface TagMapper {
    Tag toTag(TagRequestTo tagRequestTo);

    TagResponseTo toTagResponseTo(Tag tag);
}
