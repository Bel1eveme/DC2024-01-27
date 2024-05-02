using Forum.Api.Models;

namespace Forum.Api.Repositories;

public interface ICreatorRepository
{
    public Task<List<Creator>> GetAllAsync();
    
    public Task<Creator?> GetByIdAsync(long id);

    public Task<Creator> CreateAsync(Creator creatorModel);
    
    public Task<Creator?> UpdateAsync(long id, Creator updatedCreator);
    
    public Task<Creator?> DeleteAsync(long id);
}