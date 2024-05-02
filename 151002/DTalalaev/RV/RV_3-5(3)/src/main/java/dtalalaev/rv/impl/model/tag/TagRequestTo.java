package dtalalaev.rv.impl.model.tag;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.math.BigInteger;
@Getter
@Setter
@Builder
public class TagRequestTo {

    private BigInteger id;
    @Size(min =2 , max = 32, message = "Name must be between 2 and 32 characters")
    private String name;

}
