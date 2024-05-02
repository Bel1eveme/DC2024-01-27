package by.bsuir.romankokarev.mapper;

import by.bsuir.romankokarev.dto.TagRequestTo;
import by.bsuir.romankokarev.dto.TagResponseTo;
import by.bsuir.romankokarev.model.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    Tag tagRequestToTag(TagRequestTo tagRequestTo);

    TagResponseTo tagToTagResponse(Tag tag);
}
