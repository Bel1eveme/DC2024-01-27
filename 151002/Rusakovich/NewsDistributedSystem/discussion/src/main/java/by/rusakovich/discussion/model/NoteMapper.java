package by.rusakovich.discussion.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    @Mapping(target = "country", expression = "java(languageToCountry(request.language()))")
    @Mapping(target = "content", expression = "java(request.content())")
    Note mapToEntity(NoteIternalRequestTO request);

    @Mapping(target = "content", expression = "java(entity.getContent())")
    NoteResponseTO mapToResponse(Note entity);

    default String languageToCountry(String language){
        return LanguageToCountryConverter.languageToCountry(language);
    }
}
