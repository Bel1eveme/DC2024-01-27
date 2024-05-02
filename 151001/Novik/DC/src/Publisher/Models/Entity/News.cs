using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Publisher.Models.Entity;

public class News
{
    [Key]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public long id { get; set; }
    
    [ForeignKey("userId")]
    public long? userId { get; set; }
    
    [Required]
    [StringLength(64, MinimumLength = 2)]
    public String title { get; set; }
    
    [StringLength(2048, MinimumLength = 4)]
    public String? content { get; set; }
    
    public DateTime? created { get; set; }
    
    public DateTime? modified { get; set; }
    
    public User User { get; set; }
    
    public ICollection<Label> Labels = new List<Label>();
    public ICollection<NewsLabel> NewsLabels { get; set; } = new List<NewsLabel>();
}