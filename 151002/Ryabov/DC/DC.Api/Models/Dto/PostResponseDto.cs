namespace Forum.Api.Models.Dto;

public class PostResponseDto
{
    public long Id { get; set; }

    public string Content { get; set; } = string.Empty;
    
    public long StoryId { get; set; }
    
    public Story Story { get; set; }
}