namespace REST.Discussion.Models.DTOs.Request;

public class KafkaRequestDto
{
    public HttpMethod Method { get; set; } = null!;
    
    public long? Id { get; set; }
    public NoteRequestDto? Request { get; set; }
}