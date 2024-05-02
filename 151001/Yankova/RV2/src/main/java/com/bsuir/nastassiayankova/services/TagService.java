package com.bsuir.nastassiayankova.services;

import com.bsuir.nastassiayankova.beans.entity.ValidationMarker;
import com.bsuir.nastassiayankova.beans.request.TagRequestTo;
import com.bsuir.nastassiayankova.beans.response.TagResponseTo;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

public interface TagService {
    @Validated(ValidationMarker.OnCreate.class)
    TagResponseTo create(@Valid TagRequestTo entity);

    List<TagResponseTo> read();

    @Validated(ValidationMarker.OnUpdate.class)
    TagResponseTo update(@Valid TagRequestTo entity);

    void delete(Long id);

    TagResponseTo findTagById(Long Id);
}
