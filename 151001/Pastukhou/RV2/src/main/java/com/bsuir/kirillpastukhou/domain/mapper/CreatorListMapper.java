package com.bsuir.kirillpastukhou.domain.mapper;

import com.bsuir.kirillpastukhou.domain.request.CreatorRequestTo;
import com.bsuir.kirillpastukhou.domain.response.CreatorResponseTo;
import org.mapstruct.Mapper;
import com.bsuir.kirillpastukhou.domain.entity.Creator;

import java.util.List;

@Mapper(componentModel = "spring", uses = CreatorMapper.class)
public interface CreatorListMapper {
    List<Creator> toCreatorList(List<CreatorRequestTo> creatorRequestToList);

    List<CreatorResponseTo> toCreatorResponseToList(List<Creator> creatorList);
}
