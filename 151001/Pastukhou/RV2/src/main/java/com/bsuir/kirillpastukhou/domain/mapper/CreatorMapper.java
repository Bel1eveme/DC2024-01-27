package com.bsuir.kirillpastukhou.domain.mapper;

import com.bsuir.kirillpastukhou.domain.response.CreatorResponseTo;
import org.mapstruct.Mapper;
import com.bsuir.kirillpastukhou.domain.entity.Creator;
import com.bsuir.kirillpastukhou.domain.request.CreatorRequestTo;

@Mapper(componentModel = "spring", uses = TweetListMapper.class)
public interface CreatorMapper {
    Creator toCreator(CreatorRequestTo creatorRequestTo);

    CreatorResponseTo toCreatorResponseTo(Creator creator);
}
