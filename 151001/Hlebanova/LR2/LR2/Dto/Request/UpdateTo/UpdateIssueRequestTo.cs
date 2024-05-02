using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace LR2.Dto.Request.UpdateTo;

public record UpdateIssueRequestTo(
    [property: JsonPropertyName("id")] long Id,
    [property: JsonPropertyName("creatorId")]
    long CreatorId,
    [property: JsonPropertyName("title")]
    [StringLength(64, MinimumLength = 2)]
    string Title,
    [property: JsonPropertyName("content")]
    [StringLength(2048, MinimumLength = 4)]
    string Content
);