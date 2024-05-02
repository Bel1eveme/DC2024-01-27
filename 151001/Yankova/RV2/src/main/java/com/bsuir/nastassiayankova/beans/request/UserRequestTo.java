package com.bsuir.nastassiayankova.beans.request;

import jakarta.validation.constraints.Size;
import com.bsuir.nastassiayankova.beans.entity.ValidationMarker;

public record UserRequestTo(Long id,
                            @Size(min = 2, max = 64, groups = {ValidationMarker.OnCreate.class,
                                    ValidationMarker.OnUpdate.class}) String login,
                            @Size(min = 8, max = 128, groups = {ValidationMarker.OnCreate.class,
                                    ValidationMarker.OnUpdate.class}) String password,
                            @Size(min = 2, max = 64, groups = {ValidationMarker.OnCreate.class,
                                    ValidationMarker.OnUpdate.class}) String firstname,
                            @Size(min = 2, max = 64, groups = {ValidationMarker.OnCreate.class,
                                    ValidationMarker.OnUpdate.class}) String lastname) {
}
