namespace Forum.Api.Models.Dto;

public class StorySearchDto
{
    public List<string> TagNames { get; set; } = [];

    public List<int> TagIds { get; set; } = [];
    
    public string? CreatorLogin { get; set; }
    
    public string? Title { get; set; }
    
    public string? Content { get; set; }
}