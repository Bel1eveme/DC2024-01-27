using System.ComponentModel.DataAnnotations;

namespace Discussion.Models.DTOs.Requests;

public class PostRequestTo
{
    public long id { get; set; }
    
    public long? newsId { get; set; }
    
    [StringLength(2048, MinimumLength = 2)]
    public String? content { get; set; }
    
    public String? country { get; set; }

}