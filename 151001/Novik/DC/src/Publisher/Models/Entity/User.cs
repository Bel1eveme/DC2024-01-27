using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Publisher.Models.Entity;

public class User
{
    [Key]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public long id { get; set; }
    
    [StringLength(64, MinimumLength = 2)] 
    public String login { get; set; }
    
    [StringLength(128, MinimumLength = 8)]
    public String password { get; set; }
    
    [StringLength(64, MinimumLength = 2)]
    public String firstname { get; set; }
    
    [StringLength(64, MinimumLength = 2)]
    public String lastname { get; set; }
    
    public ICollection<News> News { get; set; } = new List<News>();
}