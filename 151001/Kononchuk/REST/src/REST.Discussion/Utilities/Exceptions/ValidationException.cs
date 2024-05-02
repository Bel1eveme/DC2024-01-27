namespace REST.Discussion.Utilities.Exceptions;

public class ValidationException(string message = "Incorrect data", int code = 400) : Exception(message)
{
    public int Code { get; } = code;
}