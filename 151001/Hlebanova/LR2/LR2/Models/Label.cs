using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace LR2.Models;

[Table("tbl_labels")]
[Index(nameof(Name), IsUnique = true)]
public class Label
{
    public long Id { get; set; }
    [MaxLength(32)] public string Name { get; set; } = string.Empty;
    public List<Issue> Issues { get; } = [];
}