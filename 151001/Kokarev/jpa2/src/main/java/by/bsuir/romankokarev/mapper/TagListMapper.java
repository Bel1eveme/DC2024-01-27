package by.bsuir.romankokarev.mapper;

import by.bsuir.romankokarev.dto.TagRequestTo;
import by.bsuir.romankokarev.dto.TagResponseTo;
import by.bsuir.romankokarev.model.Tag;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring", uses = TagMapper.class)
public interface TagListMapper {
    List<Tag> toTagList(List<TagRequestTo> list);

    List<TagResponseTo> toTagResponseList(List<Tag> list);
}
