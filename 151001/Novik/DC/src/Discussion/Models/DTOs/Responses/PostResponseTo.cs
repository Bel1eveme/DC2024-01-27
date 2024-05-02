namespace Discussion.Models.DTOs.Responses;

public class PostResponseTo
{
    public long id { get; set; }
    
    public long? newsId { get; set; }
    public String? content { get; set; } = String.Empty;
}