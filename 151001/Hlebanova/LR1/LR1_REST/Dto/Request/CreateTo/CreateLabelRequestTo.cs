using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace LR1.Dto.Request.CreateTo;

public record CreateLabelRequestTo(
    [property: JsonPropertyName("name")]
    [StringLength(32, MinimumLength = 2)]
    string Name
);