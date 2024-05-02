package by.rusakovich.publisher.news.model;

import by.rusakovich.publisher.author.model.Author;
import by.rusakovich.publisher.generics.model.IEntityMapper;
import by.rusakovich.publisher.generics.spi.dao.IEntityRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class NewsMapper implements IEntityMapper<Long, News, NewsRequestTO, NewsResponseTO> {
    @Autowired
    protected IEntityRepository<Long, Author> authorRepository;

    @Override
    @Mapping(target = "author", expression = "java(getAuthorById(request.authorId()))")
    public abstract News mapToEntity(NewsRequestTO request);

    public Author getAuthorById(Long id){
        Author temp = new Author();
        temp.setId(id);
        return authorRepository.readById(id).orElse(temp);
    }

    @Override
    @Mapping(target = "authorId", expression = "java(getAuthorId(entity))")
    public abstract NewsResponseTO mapToResponse(News entity);

    public Long getAuthorId(News entity){
        if(entity == null){
            return null;
        }else{
            return entity.getAuthor() != null ? entity.getAuthor().getId() : null;
        }
    }

    @Mapping(target = "creation", expression = "java(null)")
    @Mapping(target = "modification", expression = "java(null)")
    public abstract News toEntity(NewsResponseTO response);
}

