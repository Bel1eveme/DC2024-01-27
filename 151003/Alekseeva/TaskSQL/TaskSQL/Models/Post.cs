using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace TaskSQL.Models;

[Table("tbl_posts")]
public class Post
{
    public long Id { get; set; }

    [ForeignKey("Tweet")] public long? TweetId { get; set; }

    [MaxLength(2048)] public string Content { get; set; } = string.Empty;
    public Tweet? Tweet { get; set; }
}