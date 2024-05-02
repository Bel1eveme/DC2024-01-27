using Forum.Api.Models;
using Forum.Api.Repositories;

namespace Forum.Api.Services;

public class CachedPostRepository : IPostRepository
{
    public Task<List<Post>> GetAllAsync()
    {
        throw new NotImplementedException();
    }

    public Task<Post?> GetByIdAsync(long id)
    {
        throw new NotImplementedException();
    }

    public Task<Post> CreateAsync(Post postModel)
    {
        throw new NotImplementedException();
    }

    public Task<Post?> UpdateAsync(long id, Post updatedPost)
    {
        throw new NotImplementedException();
    }

    public Task<Post?> DeleteAsync(long id)
    {
        throw new NotImplementedException();
    }
}