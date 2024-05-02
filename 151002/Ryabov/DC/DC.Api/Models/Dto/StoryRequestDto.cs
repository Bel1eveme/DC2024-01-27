using System.ComponentModel.DataAnnotations;

namespace Forum.Api.Models.Dto;

public class StoryRequestDto
{
    public long Id { get; set; }
    
    [Length(2, 64, ErrorMessage = "Wrong length.")]
    public string Title { get; set; }
    
    [Length(4, 2048, ErrorMessage = "Wrong length.")]
    public string Content { get; set; }

    public DateTime Created { get; set; }

    public DateTime Modified  { get; set; }
    
    public long CreatorId { get; set; }

    public List<TagRequestDto> Tags { get; set; } = [];
}