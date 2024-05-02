namespace REST.Discussion.Utilities.Exceptions;

public class ResourceNotFoundException(string message = "Resource not found", int code = 404) : Exception(message)
{
    public int Code { get; } = code;
}