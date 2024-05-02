namespace Publisher.Models.DTOs.Responses;

public class UserResponseTo
{
    public long id { get; set; }
    
    public String login { get; set; } = String.Empty;

    public String password { get; set; } = String.Empty;
    
    public String firstname { get; set; } = String.Empty;

    public String lastname { get; set; } = String.Empty;
}