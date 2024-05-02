package com.bsuir.nastassiayankova.beans.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.bsuir.nastassiayankova.beans.entity.ValidationMarker;

public record NewsRequestTo(Long id, Long userId,
                            @Size(min = 2, max = 64, groups = {ValidationMarker.OnCreate.class,
                                    ValidationMarker.OnUpdate.class}) String title,
                            @Size(min = 4, max = 2048,
                                    groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class})
                            @Pattern(regexp = "^.*[a-zA-Z]+.*$",
                                    groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class}) String content) {
}
