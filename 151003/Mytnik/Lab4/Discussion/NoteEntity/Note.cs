using Discussion.Common;

namespace Discussion.NoteEntity
{
    public class Note(string content, string country) : AbstractEntity
    {
        public string Country { get; set; } = country;
        public int TweetId { get; set; }
        public string Content { get; set; } = content;

        public Note() : this(string.Empty, string.Empty) { }

        public Note(int id, int tweetId, string content) : this(content, string.Empty)
        {
            Id = id;
            TweetId = tweetId;
        }

        public Note(int id, int tweetId, string content, string country) : this(content, country)
        {
            Id = id;
            TweetId = tweetId;
        }
    }
}
