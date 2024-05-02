using Forum.PostApi.Models;
using Forum.PostApi.Repositories.Base;

namespace Forum.PostApi.Repositories;

public interface IPostRepository : IBaseRepository<Post, long>
{
    
}