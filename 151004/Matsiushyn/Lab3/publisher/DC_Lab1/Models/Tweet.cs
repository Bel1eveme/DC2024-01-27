using System;
using System.Collections.Generic;
using DC_Lab1.Models;

namespace DC_Lab1;

public partial class Tweet
{
    public int Id { get; set; }

    public int? authorId { get; set; }

    public string? Title { get; set; }

    public string? Content { get; set; }

    public string? Created { get; set; }

    public string? Modified { get; set; }

    public virtual Author? Author { get; set; }

    public virtual ICollection<Post> Posts { get; set; } = new List<Post>();

    public virtual ICollection<TweetLabel> TweetLabels { get; set; } = new List<TweetLabel>();

}
