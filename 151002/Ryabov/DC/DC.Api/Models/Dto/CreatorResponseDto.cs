using System.Text.Json.Serialization;

namespace Forum.Api.Models.Dto;

public class CreatorResponseDto
{
    public long Id { get; set; }

    public string Login { get; set; } = string.Empty;

    public string Password { get; set; } = string.Empty;

    [JsonPropertyName("firstname")]
    public string FirstName { get; set; } = string.Empty;

    [JsonPropertyName("lastname")]
    public string LastName { get; set; } = string.Empty;
}