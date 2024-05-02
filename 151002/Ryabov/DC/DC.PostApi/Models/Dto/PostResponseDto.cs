using Forum.PostApi.Models.Base;

namespace Forum.PostApi.Models.Dto;

public class PostResponseDto : BaseModel<long>
{
    public string Country { get; set; }
    
    public long StoryId { get; set; }
    
    public string? Content { get; set; }
}