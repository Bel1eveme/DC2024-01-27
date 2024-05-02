using Discussion.Common;

namespace Discussion.CommentEntity
{
    public class Comment(string content, string country) : AbstractEntity
    {
        public string Country { get; set; } = country;
        public int IssueId { get; set; }
        public string Content { get; set; } = content;

        public Comment() : this(string.Empty, string.Empty) { }

        public Comment(int id, int issueId, string content) : this(content, string.Empty)
        {
            Id = id;
            IssueId = issueId;
        }

        public Comment(int id, int issueId, string content, string country) : this(content, country)
        {
            Id = id;
            IssueId = issueId;
        }
    }
}
