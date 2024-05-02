package com.bsuir.nastassiayankova.beans.request;

import jakarta.validation.constraints.Size;
import com.bsuir.nastassiayankova.beans.entity.ValidationMarker;

public record TagRequestTo(Long id,
                           @Size(min = 2, max = 32, groups = {ValidationMarker.OnCreate.class,
                                   ValidationMarker.OnUpdate.class}) String name) {
}
