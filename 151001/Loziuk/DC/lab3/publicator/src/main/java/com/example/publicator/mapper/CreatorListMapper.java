package com.example.publicator.mapper;

import com.example.publicator.dto.CreatorRequestTo;
import com.example.publicator.dto.CreatorResponseTo;
import com.example.publicator.model.Creator;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = CreatorMapper.class)
public interface CreatorListMapper {
    List<Creator> toCreatorList(List<CreatorRequestTo> creators);

    List<CreatorResponseTo> toCreatorResponseList(List<Creator> creators);
}
