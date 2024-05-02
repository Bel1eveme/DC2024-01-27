using System.ComponentModel.DataAnnotations;

namespace Publisher.Models.DTOs.Requests;

public class LabelRequestTo
{
    public long id { get; set; }
    
    
    [StringLength(32, MinimumLength = 2)]
    public String? name { get; set; } = String.Empty;

}