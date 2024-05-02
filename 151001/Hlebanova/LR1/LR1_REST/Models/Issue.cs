using System.ComponentModel.DataAnnotations;

namespace LR1.Models;

public class Issue
{
    public long Id { get; set; }
    public long CreatorId { get; set; }
    [MaxLength(32)] public string Title { get; set; } = string.Empty;
    [MaxLength(2048)] public string Content { get; set; } = string.Empty;
    public DateTime Created { get; init; }
    public DateTime Modified { get; set; }
}