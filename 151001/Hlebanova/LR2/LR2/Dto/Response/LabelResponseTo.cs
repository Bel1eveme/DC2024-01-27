using System.Text.Json.Serialization;

namespace LR2.Dto.Response;

public record LabelResponseTo(
    [property: JsonPropertyName("id")] long Id,
    [property: JsonPropertyName("name")] string Name
);