package com.bsuir.nastassiayankova.mappers;

import com.bsuir.nastassiayankova.beans.entity.Tag;
import com.bsuir.nastassiayankova.beans.request.TagRequestTo;
import com.bsuir.nastassiayankova.beans.response.TagResponseTo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = TagMapper.class)
public interface TagListMapper {
    List<Tag> toTagList(List<TagRequestTo> tagRequestToList);

    List<TagResponseTo> toTagResponseToList(List<Tag> tagList);
}
