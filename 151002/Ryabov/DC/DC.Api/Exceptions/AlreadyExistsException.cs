namespace Forum.Api.Exceptions;

public class AlreadyExistsException : ArgumentException
{
    public AlreadyExistsException()
    {
    }

    public AlreadyExistsException(string message)
        : base(message)
    {
    }

    public AlreadyExistsException(string message, Exception innerException)
        : base(message, innerException)
    {
    }
}