using Publisher.Entity.Common;
using System.ComponentModel.DataAnnotations;

namespace Publisher.Entity.Db
{
    public class News(string title, string content, DateTime created, DateTime modified) : AbstractEntity
    {
        [MinLength(2)]
        public string Title { get; set; } = title;
        [MinLength(4)]
        public string Content { get; set; } = content;
        public DateTime Created { get; set; } = created;
        public DateTime Modified { get; set; } = modified;
        public ICollection<Note> Notes { get; set; } = [];
        public ICollection<Label> Labels { get; set; } = [];
        public int UserId { get; set; }
        public User User { get; set; } = null!;

        public News() : this(string.Empty, string.Empty, DateTime.Now, DateTime.Now) { }
        public News(int id, int userId, string title, string content, DateTime created, DateTime modified)
            : this(title, content, created, modified)
        {
            Id = id;
            UserId = userId;
            User = new() { Id = userId };
        }
    }
}
