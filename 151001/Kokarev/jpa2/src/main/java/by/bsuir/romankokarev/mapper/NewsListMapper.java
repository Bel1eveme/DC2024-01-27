package by.bsuir.romankokarev.mapper;

import by.bsuir.romankokarev.dto.NewsRequestTo;
import by.bsuir.romankokarev.dto.NewsResponseTo;
import by.bsuir.romankokarev.model.News;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring", uses = NewsMapper.class)
public interface NewsListMapper {
    List<News> toNewsList(List<NewsRequestTo> list);

    List<NewsResponseTo> toNewsResponseList(List<News> list);
}
