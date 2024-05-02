using Publisher.Entity.Db;
using Publisher.Repository.Interface.Common;

namespace Publisher.Repository.Interface
{
    public interface ICreatorRepository : ICrudRepository<Creator>
    {
        Tweet GetByTweetId(int tweetId);
        Task<Tweet> GetByTweetIdAsync(int tweetId);
    }
}
