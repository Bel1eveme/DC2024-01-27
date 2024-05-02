package by.rusakovich.discussion.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class BaseDSException extends RuntimeException{
    static protected Map<Integer, String> codeAndMessage  = Map.of(
            0, "Not found. ",
            1, "Can't create. ",
            2, "No conversion from request to entity available. "
    );

    private Integer errorCode;
    private String errorMessage;
}
