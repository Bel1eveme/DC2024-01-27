package by.rusakovich.discussion.error;

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
