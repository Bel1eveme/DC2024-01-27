using Publisher.Data;
using Publisher.Models;
using Publisher.Repositories.Interfaces;

namespace Publisher.Repositories.Implementations;

public class IssueSqlRepository(AppDbContext context) : BaseSqlRepository<Issue>(context), IIssueRepository
{
}