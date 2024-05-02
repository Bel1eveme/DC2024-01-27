package com.poluectov.rvproject.utils.dtoconverter;

import com.poluectov.rvproject.model.IdentifiedEntity;

public interface DtoConverter<T extends IdentifiedEntity, R extends IdentifiedEntity> {

    R convert(T t);
}
