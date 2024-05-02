package by.bsuir.taskrest.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MarkerRequestTo(
        Long id,
        @NotNull @Size(min = 2, max = 32)
        String name
) {
}
