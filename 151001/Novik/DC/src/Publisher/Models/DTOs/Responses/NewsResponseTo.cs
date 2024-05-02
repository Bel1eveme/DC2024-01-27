namespace Publisher.Models.DTOs.Responses;

public class NewsResponseTo
{
    public long id { get; set; }
    
    public long? userId { get; set; }
    
    public String title { get; set; }= String.Empty;

    public String? content { get; set; } = String.Empty;
    
    public DateTime? created { get; set; }
    
    public DateTime? modified { get; set; }
}