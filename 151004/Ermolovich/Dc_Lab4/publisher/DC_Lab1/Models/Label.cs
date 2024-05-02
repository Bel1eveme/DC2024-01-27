using System;
using System.Collections.Generic;

namespace DC_Lab1.Models;

public partial class Label
{
    public int Id { get; set; }

    public string? name { get; set; }

    public virtual ICollection<StoryLabel> StoryLabels { get; set; } = new List<StoryLabel>();

}
