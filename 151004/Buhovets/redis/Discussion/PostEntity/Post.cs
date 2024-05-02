using Discussion.Common;

namespace Discussion.CommentEntity
{
    public class Comment(string content, string country) : AbstractEntity
    {
        public string Country { get; set; } = country;
        public int TweetId { get; set; }
        public string Content { get; set; } = content;

        public Comment() : this(string.Empty, string.Empty) { }

        public Comment(int id, int tweetId, string content) : this(content, string.Empty)
        {
            Id = id;
            TweetId = tweetId;
        }

        public Comment(int id, int tweetId, string content, string country) : this(content, country)
        {
            Id = id;
            TweetId = tweetId;
        }
    }
}
