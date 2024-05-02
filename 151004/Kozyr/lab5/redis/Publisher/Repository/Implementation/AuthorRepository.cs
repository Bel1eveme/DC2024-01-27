using Publisher.Entity.Db;
using Publisher.Repository.Implementation.Common;
using Publisher.Repository.Interface;
using Publisher.Storage.Common;

namespace Publisher.Repository.Implementation
{
    public class CreatorRepository(DbStorage context) : AbstractCrudRepository<Creator>(context), ICreatorRepository
    {
        public Issue GetByIssueId(int issueId)
        {
            throw new NotImplementedException();
        }

        public Task<Issue> GetByIssueIdAsync(int issueId)
        {
            throw new NotImplementedException();
        }
    }
}
