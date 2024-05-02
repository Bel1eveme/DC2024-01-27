using Cassandra.Data.Linq;
using Cassandra.Mapping;
using Forum.PostApi.DbProvider;
using Forum.PostApi.Models;
using Forum.PostApi.Repositories.Base;

namespace Forum.PostApi.Repositories;

public class PostRepository : IPostRepository
{
    private readonly IBaseRepository<Post, long> _baseRepository;
    public PostRepository(IBaseRepository<Post, long> baseRepository)
    {
        _baseRepository = baseRepository;
    }

    public async Task<Post?> GetByIdAsync(long id)
    {
        return await _baseRepository.GetByIdAsync(id);
    }

    public async Task<IEnumerable<Post>> GetAllAsync()
    {
        return await _baseRepository.GetAllAsync();
    }

    public async Task<Post?> AddAsync(Post entity)
    {
        return await _baseRepository.AddAsync(entity);
    }

    public async Task<Post?> UpdateAsync(Post entity)
    {
        return await _baseRepository.UpdateAsync(entity);
    }

    public async Task<Post?> DeleteAsync(long id)
    {
        return await _baseRepository.DeleteAsync(id);
    }
}