namespace Publisher.Exceptions;

public static class ErrorMessages
{
	public static string CreatorNotFoundMessage(long id)
	{
		return $"Creator with id {id} was not found.";
	}

	public static string IssueNotFoundMessage(long id)
	{
		return $"Issue with id {id} was not found.";
	}

	public static string LabelNotFoundMessage(long id)
	{
		return $"Label with id {id} was not found.";
	}

	public static string NoteNotFoundMessage(long id)
	{
		return $"Note with id {id} was not found.";
	}
}