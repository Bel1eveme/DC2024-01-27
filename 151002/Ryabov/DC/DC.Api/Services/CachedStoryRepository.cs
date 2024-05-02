using Forum.Api.Models;
using Forum.Api.Repositories;

namespace Forum.Api.Services;

public class CachedStoryRepository : IStoryRepository
{
    public Task<List<Story>> GetAllAsync()
    {
        throw new NotImplementedException();
    }

    public IQueryable<Story> GetAllWithFilteringAsync()
    {
        throw new NotImplementedException();
    }

    public Task<Story?> GetByIdAsync(long id)
    {
        throw new NotImplementedException();
    }

    public Task<Story> CreateAsync(Story creatorModel)
    {
        throw new NotImplementedException();
    }

    public Task<Story?> UpdateAsync(long id, Story updatedStory)
    {
        throw new NotImplementedException();
    }

    public Task<Story?> DeleteAsync(long id)
    {
        throw new NotImplementedException();
    }
}