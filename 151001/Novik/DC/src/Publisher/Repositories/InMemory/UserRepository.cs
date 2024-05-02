using Publisher.Models.Entity;
using Publisher.Repositories.interfaces;
using REST.Repositories.InMemory.sample;

namespace Publisher.Repositories.InMemory;

public class UserRepository : BaseRepository<User>, IUserRepository
{
    public Task<User> AddAsync(User entity)
    {
        var id = GlobalId++;
        entity.id = id;
        _entities.Add(id,entity);
        return Task.FromResult(entity);
    }

    public Task<User> UpdateAsync(User entity)
    {
        var id = entity.id;
        _entities[id] = entity;
        return Task.FromResult(_entities[id]);
    }
}