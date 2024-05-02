namespace REST.Publisher.Models.DTOs.Response;

public class KafkaResponseDto
{
    public NoteResponseDto? Response { get; set; }
    public List<NoteResponseDto>? ResponseList { get; set; }
    public ErrorResponseDto? Error { get; set; }
}