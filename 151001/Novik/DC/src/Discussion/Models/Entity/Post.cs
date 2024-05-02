using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Discussion.Models.Entity;

public class Post
{

    public long id { get; set; }
    
    public long? newsId { get; set; }
    
    public String? content { get; set; }
    
    public String? country { get; set; }
    
}