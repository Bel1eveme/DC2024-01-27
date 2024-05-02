package com.example.publicator.mapper;

import com.example.publicator.dto.TagRequestTo;
import com.example.publicator.dto.TagResponseTo;
import com.example.publicator.model.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    Tag tagRequestToTag(TagRequestTo tagRequestTo);

    TagResponseTo tagToTagResponse(Tag tag);
}
