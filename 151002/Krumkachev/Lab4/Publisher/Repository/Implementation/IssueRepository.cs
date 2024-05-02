using Publisher.Entity.Db;
using Publisher.Repository.Implementation.Common;
using Publisher.Repository.Interface;
using Publisher.Storage.Common;

namespace Publisher.Repository.Implementation
{
    public class IssueRepository(DbStorage context) : AbstractCrudRepository<Issue>(context), IIssueRepository
    {
        private readonly DbStorage _context = context;

        public override Issue Add(Issue issue)
        {
            var creator = new Creator { Id = issue.CreatorId };
            _context.Creators.Attach(creator);
            issue.Creator = creator;

            return base.Add(issue);
        }

        public override async Task<Issue> AddAsync(Issue issue)
        {
            var creator = new Creator { Id = issue.CreatorId };
            _context.Creators.Attach(creator);
            issue.Creator = creator;

            return await base.AddAsync(issue);
        }
    }
}
