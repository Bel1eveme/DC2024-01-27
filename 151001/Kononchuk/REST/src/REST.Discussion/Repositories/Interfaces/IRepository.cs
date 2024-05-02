using REST.Discussion.Utilities.Exceptions;

namespace REST.Discussion.Repositories.Interfaces;

public interface IRepository<TKey, TEntity> where TEntity : class
{
    /// <summary>
    /// Creates a new entity
    /// </summary>
    /// <exception cref="ArgumentNullException">entity is null</exception>>
    public Task<TEntity> AddAsync(TEntity entity);
    
    /// <summary>
    /// Returns an entity by given id
    /// </summary>
    /// <exception cref="ResourceNotFoundException">Occurs if an entity with the specified ID does not exist</exception>
    public Task<TEntity> GetByIdAsync(TKey id);
    
    public Task<IEnumerable<TEntity>> GetAllAsync();
    
    /// <summary>
    /// Updates an entity with the given id and the given values
    /// </summary>
    /// <exception cref="ResourceNotFoundException">Occurs if an entity with the specified ID does not exist</exception>
    /// <exception cref="ArgumentNullException">entity is null</exception>>
    public Task<TEntity> UpdateAsync(TKey id, TEntity entity);

    /// <summary>
    /// Delete an entity with the given id
    /// </summary>
    /// <exception cref="ResourceNotFoundException">Occurs if an entity with the specified ID does not exist</exception>
    public Task DeleteAsync(TKey id);
}