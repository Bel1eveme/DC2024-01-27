using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace LR2.Models;

[Table("tbl_comments")]
public class Comment
{
    public long Id { get; set; }

    [ForeignKey("Issue")] public long? IssueId { get; set; }

    [MaxLength(2048)] public string Content { get; set; } = string.Empty;
    public Issue? Issue { get; set; }
}