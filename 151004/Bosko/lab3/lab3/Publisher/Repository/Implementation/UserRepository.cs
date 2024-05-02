using Publisher.Entity.Db;
using Publisher.Repository.Implementation.Common;
using Publisher.Repository.Interface;
using Publisher.Storage.Common;

namespace Publisher.Repository.Implementation
{
    public class UserRepository(DbStorage context) : AbstractCrudRepository<User>(context), IUserRepository
    {
        public News GetByNewsId(int newsId)
        {
            throw new NotImplementedException();
        }

        public Task<News> GetByNewsIdAsync(int newsId)
        {
            throw new NotImplementedException();
        }
    }
}
