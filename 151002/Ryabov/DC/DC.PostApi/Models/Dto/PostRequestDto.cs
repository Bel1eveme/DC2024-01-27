using System.ComponentModel.DataAnnotations;
using Forum.PostApi.Models.Base;

namespace Forum.PostApi.Models.Dto;

public class PostRequestDto : BaseModel<long>
{
    public string? Country { get; set; }
    
    public long StoryId { get; set; }
    
    public string? Content { get; set; }
}