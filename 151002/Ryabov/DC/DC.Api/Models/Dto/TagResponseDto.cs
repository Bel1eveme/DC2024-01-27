namespace Forum.Api.Models.Dto;

public class TagResponseDto
{
    public int Id { get; set; }

    public string Name { get; set; } = string.Empty;
    
    public List<StoryResponseDto> Stories { get; set; }
}