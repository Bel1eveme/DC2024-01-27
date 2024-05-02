package by.rusakovich.publisher.author.model;

import by.rusakovich.publisher.generics.model.IEntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper extends IEntityMapper<Long, Author, AuthorRequestTO, AuthorResponseTO> {
    @Override
    Author mapToEntity(AuthorRequestTO request);
    @Override
    AuthorResponseTO mapToResponse(Author entity);
}
