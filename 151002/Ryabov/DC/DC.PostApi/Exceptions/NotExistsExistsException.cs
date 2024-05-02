namespace Forum.PostApi.Exceptions;

public class NotExistsException : ArgumentException
{
    public NotExistsException()
    {
    }

    public NotExistsException(string message)
        : base(message)
    {
    }

    public NotExistsException(string message, Exception innerException)
        : base(message, innerException)
    {
    }
}