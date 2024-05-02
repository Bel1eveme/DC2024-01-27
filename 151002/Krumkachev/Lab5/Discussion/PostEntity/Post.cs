using Discussion.Common;

namespace Discussion.PostEntity
{
    public class Post(string content, string country) : AbstractEntity
    {
        public string Country { get; set; } = country;
        public int IssueId { get; set; }
        public string Content { get; set; } = content;

        public Post() : this(string.Empty, string.Empty) { }

        public Post(int id, int issueId, string content) : this(content, string.Empty)
        {
            Id = id;
            IssueId = issueId;
        }

        public Post(int id, int issueId, string content, string country) : this(content, country)
        {
            Id = id;
            IssueId = issueId;
        }
    }
}
