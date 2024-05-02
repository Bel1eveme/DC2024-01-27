using Publisher.Models.DTOs.Responses;

namespace Publisher.Exceptions;

public class KafkaException(int code, ErrorResponseDto error): Exception(error.ErrorMessage)
{
    public int Code { get; } = code;
    public ErrorResponseDto ErrorResponse { get; set; } = error;
}