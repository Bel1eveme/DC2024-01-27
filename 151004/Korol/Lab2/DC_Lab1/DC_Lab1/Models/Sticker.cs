using System;
using System.Collections.Generic;

namespace DC_Lab1.Models;

public partial class Sticker
{
    public int Id { get; set; }

    public string? name { get; set; }

    public virtual ICollection<StorySticker> StoryStickers { get; set; } = new List<StorySticker>();

}
