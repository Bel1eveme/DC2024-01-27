package com.example.publicator.mapper;

import com.example.publicator.dto.CreatorRequestTo;
import com.example.publicator.dto.CreatorResponseTo;
import com.example.publicator.model.Creator;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreatorMapper {
    Creator creatorRequestToCreator(CreatorRequestTo creatorRequestTo);

    CreatorResponseTo creatorToCreatorResponse(Creator creator);
}
