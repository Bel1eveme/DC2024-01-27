using System.Text.Json.Serialization;

namespace LR2.Dto.Response;

public record CommentResponseTo(
    [property: JsonPropertyName("id")] long Id,
    [property: JsonPropertyName("issueId")]
    long IssueId,
    [property: JsonPropertyName("content")]
    string Content
);