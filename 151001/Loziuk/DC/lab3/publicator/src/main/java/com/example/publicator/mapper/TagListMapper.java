package com.example.publicator.mapper;

import com.example.publicator.dto.TagRequestTo;
import com.example.publicator.dto.TagResponseTo;
import com.example.publicator.model.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = TagMapper.class)
public interface TagListMapper {
    List<Tag> toTagList(List<TagRequestTo> list);

    List<TagResponseTo> toTagResponseList(List<Tag> list);
}
