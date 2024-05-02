using DC_Lab1.Models;
using System;
using System.Collections.Generic;

namespace DC_Lab1;

public partial class StoryLabel
{
    public int Id { get; set; }

    public int? storyId { get; set; }

    public int? labelId { get; set; }

    public virtual Label? Label { get; set; }

    public virtual Story? Story { get; set; }
}
