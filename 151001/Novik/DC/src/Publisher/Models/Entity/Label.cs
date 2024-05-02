using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Publisher.Models.Entity;

public class Label
{
    [Key]
   
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    
    public long id { get; set; }
    
    [StringLength(32, MinimumLength = 2)]
    public String? name { get; set; }

    public ICollection<News> News { get; set; }= new List<News>();

    public ICollection<NewsLabel> NewsLabels { get; set; }= new List<NewsLabel>();
}