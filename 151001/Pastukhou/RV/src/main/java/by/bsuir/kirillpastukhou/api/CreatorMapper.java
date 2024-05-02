package by.bsuir.kirillpastukhou.api;

import by.bsuir.kirillpastukhou.impl.bean.Creator;
import by.bsuir.kirillpastukhou.impl.dto.CreatorRequestTo;
import by.bsuir.kirillpastukhou.impl.dto.CreatorResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreatorMapper {
    CreatorMapper INSTANCE = Mappers.getMapper(CreatorMapper.class);

    CreatorResponseTo CreatorToCreatorResponseTo(Creator creator);

    CreatorRequestTo CreatorToCreatorRequestTo(Creator creator);

    Creator CreatorResponseToToCreator(CreatorResponseTo creatorResponseTo);

    Creator CreatorRequestToToCreator(CreatorRequestTo creatorRequestTo);
}
