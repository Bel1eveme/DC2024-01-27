package com.bsuir.kirillpastukhou.serivces;

import com.bsuir.kirillpastukhou.domain.entity.ValidationMarker;
import com.bsuir.kirillpastukhou.domain.request.TagRequestTo;
import com.bsuir.kirillpastukhou.domain.response.TagResponseTo;
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
