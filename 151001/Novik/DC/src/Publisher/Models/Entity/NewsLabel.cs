using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Publisher.Models.Entity;

public class NewsLabel
{
    [Key]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public long id { get; set; }
    
    [ForeignKey("newsId")]
    public long newsId { get; set; }
    
    [ForeignKey("labelId")]
    public long labelId { get; set; }
    
    public News News { get; set; }
    
    public Label Label { get; set; }
}