package com.bsuir.kirillpastukhou.serivces;

import com.bsuir.kirillpastukhou.domain.entity.Creator;
import com.bsuir.kirillpastukhou.domain.entity.ValidationMarker;
import com.bsuir.kirillpastukhou.domain.request.CreatorRequestTo;
import com.bsuir.kirillpastukhou.domain.response.CreatorResponseTo;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

public interface CreatorService {
    @Validated(ValidationMarker.OnCreate.class)
    CreatorResponseTo create(@Valid CreatorRequestTo entity);

    List<CreatorResponseTo> read();

    @Validated(ValidationMarker.OnUpdate.class)
    CreatorResponseTo update(@Valid CreatorRequestTo entity);

    void delete(Long id);

    CreatorResponseTo findCreatorById(Long id);

    Optional<Creator> findCreatorByIdExt(Long id);
}
