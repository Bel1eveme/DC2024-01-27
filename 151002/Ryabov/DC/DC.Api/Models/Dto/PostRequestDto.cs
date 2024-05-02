using System.ComponentModel.DataAnnotations;

namespace Forum.Api.Models.Dto;

public class PostRequestDto
{
    public long Id { get; set; }

    [Length(2, 2048, ErrorMessage = "Wrong content length.")]
    public string Content { get; set; }
    
    public long StoryId { get; set; }
}