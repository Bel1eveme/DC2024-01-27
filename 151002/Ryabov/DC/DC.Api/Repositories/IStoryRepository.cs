using Forum.Api.Models;
using Forum.Api.Models.Dto;

namespace Forum.Api.Repositories;

public interface IStoryRepository
{
    public Task<List<Story>> GetAllAsync();
    
    public IQueryable<Story> GetAllWithFilteringAsync();
    
    public Task<Story?> GetByIdAsync(long id);

    public Task<Story> CreateAsync(Story creatorModel);
    
    public Task<Story?> UpdateAsync(long id, Story updatedStory);
    
    public Task<Story?> DeleteAsync(long id);
}