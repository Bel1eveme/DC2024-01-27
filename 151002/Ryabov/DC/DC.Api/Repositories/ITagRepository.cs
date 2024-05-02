using Forum.Api.Models;

namespace Forum.Api.Repositories;

public interface ITagRepository
{
    public Task<List<Tag>> GetAllAsync();
    
    public Task<Tag?> GetByIdAsync(long id);

    public Task<Tag> CreateAsync(Tag tagModel);
    
    public Task<Tag?> UpdateAsync(long id, Tag updatedTag);
    
    public Task<Tag?> DeleteAsync(long id);
}