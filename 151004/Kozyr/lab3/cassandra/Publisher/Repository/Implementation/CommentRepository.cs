using Publisher.Entity.Db;
using Publisher.Repository.Implementation.Common;
using Publisher.Repository.Interface;
using Publisher.Storage.Common;

namespace Publisher.Repository.Implementation
{
    public class CommentRepository(DbStorage context) : AbstractCrudRepository<Comment>(context), ICommentRepository
    {
        private readonly DbStorage _context = context;

        public override Comment Add(Comment comment)
        {
            var issue = new Issue { Id = comment.IssueId };
            _context.Issues.Attach(issue);
            comment.Issue = issue;

            return base.Add(comment);
        }

        public override async Task<Comment> AddAsync(Comment comment)
        {
            var issue = new Issue { Id = comment.IssueId };
            _context.Issues.Attach(issue);
            comment.Issue = issue;

            return await base.AddAsync(comment);
        }
    }
}
