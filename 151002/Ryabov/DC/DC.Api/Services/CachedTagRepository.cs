using Forum.Api.Models;
using Forum.Api.Repositories;

namespace Forum.Api.Services;

public class CachedTagRepository : ITagRepository
{
    public Task<List<Tag>> GetAllAsync()
    {
        throw new NotImplementedException();
    }

    public Task<Tag?> GetByIdAsync(long id)
    {
        throw new NotImplementedException();
    }

    public Task<Tag> CreateAsync(Tag tagModel)
    {
        throw new NotImplementedException();
    }

    public Task<Tag?> UpdateAsync(long id, Tag updatedTag)
    {
        throw new NotImplementedException();
    }

    public Task<Tag?> DeleteAsync(long id)
    {
        throw new NotImplementedException();
    }
}