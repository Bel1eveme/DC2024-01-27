package com.bsuir.kirillpastukhou.domain.request;

import jakarta.validation.constraints.Size;
import com.bsuir.kirillpastukhou.domain.entity.ValidationMarker;

public record TagRequestTo(
        Long id,
        @Size(min = 3, max = 32, groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class})
        String name) {
}
