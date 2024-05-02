using Publisher.Entity.Db;
using Publisher.Repository.Interface.Common;

namespace Publisher.Repository.Interface
{
    public interface ICreatorRepository : ICrudRepository<Creator>
    {
        Issue GetByIssueId(int issueId);
        Task<Issue> GetByIssueIdAsync(int issueId);
    }
}
