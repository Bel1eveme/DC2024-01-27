using REST.Publisher.Models.DTOs.Response;

namespace REST.Publisher.Utilities.Exceptions;

public class KafkaException(int code, ErrorResponseDto error): Exception(error.ErrorMessage)
{
    public int Code { get; } = code;
    public ErrorResponseDto ErrorResponse { get; set; } = error;
}