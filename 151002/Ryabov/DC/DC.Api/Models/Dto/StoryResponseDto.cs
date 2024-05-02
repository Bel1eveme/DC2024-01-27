namespace Forum.Api.Models.Dto;

public class StoryResponseDto
{
    public long Id { get; set; }

    public string Title { get; set; } = string.Empty;
    
    public string Content { get; set; } = string.Empty;

    public DateTime Created { get; set; }

    public DateTime Modified  { get; set; }

    public long CreatorId { get; set; }
    
    public Creator Creator { get; set; }
    
    public List<TagResponseDto> Tags { get; set; }
}