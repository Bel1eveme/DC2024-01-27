using Discussion.Common;

namespace Discussion.NoteEntity
{
    public class Note(string content, string country) : AbstractEntity
    {
        public string Country { get; set; } = country;
        public int NewsId { get; set; }
        public string Content { get; set; } = content;

        public Note() : this(string.Empty, string.Empty) { }

        public Note(int id, int newsId, string content) : this(content, string.Empty)
        {
            Id = id;
            NewsId = newsId;
        }

        public Note(int id, int newsId, string content, string country) : this(content, country)
        {
            Id = id;
            NewsId = newsId;
        }
    }
}
