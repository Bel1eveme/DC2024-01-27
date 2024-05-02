package by.rusakovich.publisher.error.exception;

public class CantCreate extends BaseDSException{
    public CantCreate(){super(1, BaseDSException.codeAndMessage.get(1));}
    public CantCreate(String add){super(1, BaseDSException.codeAndMessage.get(1) + add);}
    public CantCreate(Object id){super(1, BaseDSException.codeAndMessage.get(1) + "With id: " + id.toString());}
}
