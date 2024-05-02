using System.ComponentModel.DataAnnotations;

namespace Publisher.Models.DTOs.Requests;

public class UserRequestTo
{
    public long id { get; set; }
    
    [Required]
    [StringLength(64, MinimumLength = 2)] 
    public String login { get; set; } = String.Empty;

    [Required]
    [StringLength(128, MinimumLength = 8)]
    public String password { get; set; } = String.Empty;
    
    [Required]
    [StringLength(64, MinimumLength = 2)] 
    public String firstname { get; set; } = String.Empty;

    [Required]
    [StringLength(64, MinimumLength = 2)] 
    public String lastname { get; set; } = String.Empty;
    
}