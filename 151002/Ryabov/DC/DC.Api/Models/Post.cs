using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace Forum.Api.Models;

public class Post
{
    public long Id { get; set; }
    
    public string Content { get; set; }
    
    public Story Story { get; set; }
    
    public long StoryId { get; set; }
}