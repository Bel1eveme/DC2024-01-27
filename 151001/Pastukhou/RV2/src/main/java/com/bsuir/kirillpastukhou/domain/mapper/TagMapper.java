package com.bsuir.kirillpastukhou.domain.mapper;

import com.bsuir.kirillpastukhou.domain.entity.Tag;
import com.bsuir.kirillpastukhou.domain.request.TagRequestTo;
import com.bsuir.kirillpastukhou.domain.response.TagResponseTo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TweetListMapper.class)
public interface TagMapper {
    Tag toTag(TagRequestTo tagRequestTo);

    TagResponseTo toTagResponseTo(Tag tag);
}
