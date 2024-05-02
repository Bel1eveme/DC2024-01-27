using Forum.Api.Models;
using Forum.Api.Models.Dto;

namespace Forum.Api.Repositories;

public interface IPostRepository
{
    public Task<List<Post>> GetAllAsync();
    
    public Task<Post?> GetByIdAsync(long id);

    public Task<Post> CreateAsync(Post postModel);
    
    public Task<Post?> UpdateAsync(long id, Post updatedPost);
    
    public Task<Post?> DeleteAsync(long id);
}