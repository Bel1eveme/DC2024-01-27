using System;
using System.Collections.Generic;

namespace DC_Lab1.Models;

public partial class Post
{
    public int Id { get; set; }

    public int? storyId { get; set; }

    public string? Content { get; set; }

    public virtual Story? Story { get; set; }
}
