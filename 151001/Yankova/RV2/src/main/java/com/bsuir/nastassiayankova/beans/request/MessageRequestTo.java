package com.bsuir.nastassiayankova.beans.request;

import jakarta.validation.constraints.Size;
import com.bsuir.nastassiayankova.beans.entity.ValidationMarker;

public record MessageRequestTo(Long id, Long newsId,
                               @Size(min = 2, max = 2048, groups = {ValidationMarker.OnCreate.class,
                                       ValidationMarker.OnUpdate.class}) String content) {
}
