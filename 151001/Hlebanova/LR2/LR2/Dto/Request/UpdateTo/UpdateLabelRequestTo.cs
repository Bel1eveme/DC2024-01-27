using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace LR2.Dto.Request.UpdateTo;

public record UpdateLabelRequestTo(
    [property: JsonPropertyName("id")] long Id,
    [property: JsonPropertyName("name")]
    [StringLength(32, MinimumLength = 2)]
    string Name
);