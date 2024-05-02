using Publisher.Data;
using Publisher.Models;
using Publisher.Repositories.Interfaces;

namespace Publisher.Repositories.Implementations;

public class LabelSqlRepository(AppDbContext context) : BaseSqlRepository<Label>(context), ILabelRepository
{
}