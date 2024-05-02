using System;
using System.Collections.Generic;
using DC_Lab1.Models;

namespace DC_Lab1;

public partial class Story
{
    public int Id { get; set; }

    public int? authorId { get; set; }

    public string? Title { get; set; }

    public string? Content { get; set; }

    public string? Created { get; set; }

    public string? Modified { get; set; }

    public virtual Author? Author { get; set; }

    public virtual ICollection<Message> Messages { get; set; } = new List<Message>();

    public virtual ICollection<StorySticker> StoryStickers { get; set; } = new List<StorySticker>();

}
