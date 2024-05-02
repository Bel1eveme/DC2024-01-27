using Publisher.Repositories.interfaces;

namespace REST.Repositories.InMemory.sample;

public abstract class BaseRepository<TEntity> : IRepository<TEntity> where TEntity: class
{
    protected long GlobalId = 1;
    protected readonly Dictionary<long, TEntity> _entities = new Dictionary<long, TEntity>();
    

    public Task<TEntity?> GetByIdAsync(long id)
    {
        return Task.FromResult(_entities.ContainsKey(id) ? _entities[id] : null);
    }

    public Task<IEnumerable<TEntity>> GetAllAsync()
    {
        return Task.FromResult<IEnumerable<TEntity>>(_entities.Values);
    }

    public Task<TEntity> AddAsync(TEntity entity)
    {
        var id = GlobalId++;
        _entities.Add(id,entity);
        return Task.FromResult(entity);
    }

    public Task<TEntity> UpdateAsync(TEntity entity)
    {
        var idProperty = typeof(TEntity).GetProperty("id");
        if (idProperty == null)
        {
            throw new InvalidOperationException("Entity does not have an Id property.");
        }

        var idValue = (long)idProperty.GetValue(entity);
        _entities[idValue] = entity;

        return Task.FromResult(_entities[idValue]);
    }

    public Task<bool> Exists(long id)
    {
        throw new NotImplementedException();
    }

    public Task DeleteAsync(long id)
    {
        _entities.Remove(id);
        return Task.CompletedTask;
    }
}