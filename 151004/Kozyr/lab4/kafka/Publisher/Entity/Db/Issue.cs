using Publisher.Entity.Common;
using System.ComponentModel.DataAnnotations;

namespace Publisher.Entity.Db
{
    public class Issue(string title, string content, DateTime created, DateTime modified) : AbstractEntity
    {
        [MinLength(2)]
        public string Title { get; set; } = title;
        [MinLength(4)]
        public string Content { get; set; } = content;
        public DateTime Created { get; set; } = created;
        public DateTime Modified { get; set; } = modified;
        public ICollection<Marker> Markers { get; set; } = [];
        public int CreatorId { get; set; }
        public Creator Creator { get; set; } = null!;

        public Issue() : this(string.Empty, string.Empty, DateTime.Now, DateTime.Now) { }
        public Issue(int id, int creatorId, string title, string content, DateTime created, DateTime modified)
            : this(title, content, created, modified)
        {
            Id = id;
            CreatorId = creatorId;
            Creator = new() { Id = creatorId };
        }
    }
}
