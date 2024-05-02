namespace Forum.Api.Exceptions;

public class StoryNotExists : ArgumentException
{
    public StoryNotExists()
    {
    }

    public StoryNotExists(string message)
        : base(message)
    {
    }

    public StoryNotExists(string message, Exception innerException)
        : base(message, innerException)
    {
    }
}