using System;
using System.Collections.Generic;

namespace DC_Lab1.Models;

public partial class Marker
{
    public int Id { get; set; }

    public string? Name { get; set; }

    public virtual ICollection<StoryLabel> TweetMarkers { get; set; } = new List<StoryLabel>();

}
