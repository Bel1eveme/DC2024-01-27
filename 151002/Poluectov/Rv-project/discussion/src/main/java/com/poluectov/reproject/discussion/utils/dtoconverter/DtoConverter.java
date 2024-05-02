package com.poluectov.reproject.discussion.utils.dtoconverter;


import com.poluectov.reproject.discussion.model.IdentifiedEntity;

public interface DtoConverter<T extends IdentifiedEntity, R extends IdentifiedEntity> {

    R convert(T t);
}
