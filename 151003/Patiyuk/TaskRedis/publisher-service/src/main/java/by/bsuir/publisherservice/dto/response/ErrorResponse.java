package by.bsuir.publisherservice.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        String message,
        String code,
        LocalDateTime timestamp
) {
    public static class ErrorResponseBuilder {
        public ErrorResponseBuilder code(HttpStatus status, int subCode) {
            this.code = String.valueOf(status.value() + subCode);
            return this;
        }
    }
}
