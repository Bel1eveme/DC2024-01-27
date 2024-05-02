package dtalalaev.rv.impl.exeptions;
import lombok.AllArgsConstructor;
import lombok.Data;
public class ApiError {
    private String errorMessage;
    private Integer errorCode;
}