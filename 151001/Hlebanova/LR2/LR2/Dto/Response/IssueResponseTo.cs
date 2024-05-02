using System.Text.Json.Serialization;

namespace LR2.Dto.Response;

public record IssueResponseTo(
    [property: JsonPropertyName("id")] long Id,
    [property: JsonPropertyName("creatorId")]
    long CreatorId,
    [property: JsonPropertyName("title")] string Title,
    [property: JsonPropertyName("content")]
    string Content
);