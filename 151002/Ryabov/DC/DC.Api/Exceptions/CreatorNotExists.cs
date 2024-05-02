namespace Forum.Api.Exceptions;

public class CreatorNotExists : ArgumentException
{
    public CreatorNotExists()
    {
    }

    public CreatorNotExists(string message)
        : base(message)
    {
    }

    public CreatorNotExists(string message, Exception innerException)
        : base(message, innerException)
    {
    }
}