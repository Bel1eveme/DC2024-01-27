package dtalalaev.rv.impl.exeptions;

import java.math.BigInteger;
import java.util.List;

public class EntititesNotFoundException extends Exception {

    public EntititesNotFoundException(String entity, List<BigInteger> ids) {
        super(entity + " with ids " + ids + " not found");
    }
}