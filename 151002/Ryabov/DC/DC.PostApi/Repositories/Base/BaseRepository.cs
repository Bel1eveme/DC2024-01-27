using Cassandra.Mapping;
using Forum.PostApi.DbProvider;
using Forum.PostApi.Models.Base;

namespace Forum.PostApi.Repositories.Base;

public class BaseRepository<T, TKey> : IBaseRepository<T, TKey> where T : BaseModel<TKey>
                                                                where TKey : notnull
{
    private readonly Mapper _mapper;
    
    public BaseRepository(ICassandraProvider cassandraProvider)
    {
        _mapper = new Mapper(cassandraProvider.GetSession());
    }

    public async Task<T?> GetByIdAsync(TKey id)
    {
        return await _mapper.FirstOrDefaultAsync<T>($"WHERE id = {id} ALLOW FILTERING");
    }

    public async Task<IEnumerable<T>> GetAllAsync()
    {
        return await _mapper.FetchAsync<T>();
    }

    public async Task<T?> AddAsync(T entity)
    {
        var addStatement = await _mapper.InsertIfNotExistsAsync(entity);
        
        //return addStatement.Applied? addStatement.Existing : null;
        return entity;
    }

    public async Task<T?> UpdateAsync(T entity)
    {
        var existingEntity = await GetByIdAsync(entity.Id);

        if (existingEntity == null) return null;
        
        await _mapper.UpdateAsync(entity);
        return entity;
    }

    public async Task<T?> DeleteAsync(TKey id)
    {
        var entity = await GetByIdAsync(id);

        if (entity == null) return null;
        
        await _mapper.DeleteAsync(entity);
        return entity;
    }
}