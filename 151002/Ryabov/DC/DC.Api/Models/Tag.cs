using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace Forum.Api.Models;

public class Tag
{
    public long Id { get; set; }
    
    public string Name { get; set; }
    
    public List<Story> Stories { get; set; }
}