using System.ComponentModel.DataAnnotations;

namespace LR1.Models;

public class Comment
{
    public long Id { get; set; }
    public long IssueId { get; set; }
    [MaxLength(2048)] public string Content { get; set; } = string.Empty;
}