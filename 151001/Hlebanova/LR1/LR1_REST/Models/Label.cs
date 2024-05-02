using System.ComponentModel.DataAnnotations;

namespace LR1.Models;

public class Label
{
    public long Id { get; set; }
    [MaxLength(32)] public string Name { get; set; } = string.Empty;
}