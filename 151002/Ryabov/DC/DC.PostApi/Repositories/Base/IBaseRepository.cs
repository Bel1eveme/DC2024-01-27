using Forum.PostApi.Models.Base;

namespace Forum.PostApi.Repositories.Base;

public interface IBaseRepository<T, in TKey> where T : ICassandraModel<TKey> where TKey : notnull
{
    Task<T?> GetByIdAsync(TKey id);
    Task<IEnumerable<T>> GetAllAsync();
    Task<T?> AddAsync(T entity);
    Task<T?> UpdateAsync(T entity);
    Task<T?> DeleteAsync(TKey id);
}