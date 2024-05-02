using Publisher.Entity.Common;
using System.ComponentModel.DataAnnotations;

namespace Publisher.Entity.Db
{
    public class Tweet(string title, string content, DateTime created, DateTime modified) : AbstractEntity
    {
        [MinLength(2)]
        public string Title { get; set; } = title;
        [MinLength(4)]
        public string Content { get; set; } = content;
        public DateTime Created { get; set; } = created;
        public DateTime Modified { get; set; } = modified;
        public ICollection<Post> Posts { get; set; } = [];
        public ICollection<Sticker> Stickers { get; set; } = [];
        public int EditorId { get; set; }
        public Editor Editor { get; set; } = null!;

        public Tweet() : this(string.Empty, string.Empty, DateTime.Now, DateTime.Now) { }
        public Tweet(int id, int editorId, string title, string content, DateTime created, DateTime modified)
            : this(title, content, created, modified)
        {
            Id = id;
            EditorId = editorId;
            Editor = new() { Id = editorId };
        }
    }
}
