package by.rusakovich.publisher.error.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseTO {
    private String errorCode;
    private String errorMessage;
}
