using Publisher.Entity.Db;
using Publisher.Repository.Interface.Common;

namespace Publisher.Repository.Interface
{
    public interface INewsRepository : ICrudRepository<News>
    {
    }
}
