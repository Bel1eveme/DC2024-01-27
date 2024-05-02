package com.bsuir.kirillpastukhou.domain.mapper;

import com.bsuir.kirillpastukhou.domain.entity.Tag;
import com.bsuir.kirillpastukhou.domain.request.TagRequestTo;
import com.bsuir.kirillpastukhou.domain.response.TagResponseTo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = TagMapper.class)
public interface TagListMapper {
    List<Tag> toTagList(List<TagRequestTo> tagRequestToList);

    List<TagResponseTo> toTagResponseToList(List<Tag> tagList);
}
