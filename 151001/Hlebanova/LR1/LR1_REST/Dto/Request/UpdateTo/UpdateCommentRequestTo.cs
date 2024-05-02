using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace LR1.Dto.Request.UpdateTo;

public record UpdateCommentRequestTo(
    [property: JsonPropertyName("id")] long Id,
    [property: JsonPropertyName("issueId")]
    long IssueId,
    [property: JsonPropertyName("content")]
    [StringLength(2048, MinimumLength = 2)]
    string Content
);