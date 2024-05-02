using Publisher.Models.Entity;
using Publisher.Repositories.interfaces;
using REST.Repositories.InMemory.sample;

namespace Publisher.Repositories.InMemory;

public class NewsRepository : BaseRepository<News>, INewsRepository
{
    public Task<News> AddAsync(News entity)
    {
        var id = GlobalId++;
        entity.id = id;
        _entities.Add(id,entity);
        return Task.FromResult(entity);
    }

    public Task<News> UpdateAsync(News entity)
    {
        var id = entity.id;
        _entities[id] = entity;
        return Task.FromResult(_entities[id]);
    }
}