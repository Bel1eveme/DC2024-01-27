using Discussion.Models.DTOs.Requests;

namespace Publisher.Models.DTOs.Requests;

public class KafkaRequestDto
{
    public HttpMethod Method { get; set; } = null!;
    
    public long? Id { get; set; }
    public PostRequestTo? Request { get; set; }
}