namespace REST.Discussion.Utilities.Exceptions;

public class AssociationException(string message = "Forbid due incorrect value in association field", int code = 403) : Exception(message)
{
    public int Code { get; } = code;
}