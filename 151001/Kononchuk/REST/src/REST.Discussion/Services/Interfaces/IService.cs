using REST.Discussion.Repositories.Interfaces;
using REST.Discussion.Utilities.Exceptions;

namespace REST.Discussion.Services.Interfaces;

public interface IService<TRequest, TResponse, TKey>
{
    /// <summary>
    /// Creates a new entity
    /// </summary>
    /// <exception cref="ArgumentNullException">entity is null</exception>
    /// <exception cref="ValidationException">Occurs if the entity does not pass validation</exception>
    /// See <see cref="IRepository{TKey,TEntity}.AddAsync"/> for the possible source of the exception.</exception>
    public Task<TResponse> CreateAsync(TRequest dto);

    /// <summary>
    /// Returns an entity by given id
    /// </summary>
    /// <exception cref="IRepository{TKey,TEntity}.GetByIdAsync">Occurs if an entity with the specified ID does not exist.
    /// See <see cref="IRepository{TKey,TEntity}"/> for the possible source of the exception.</exception>
    public Task<TResponse> GetByIdAsync(TKey id);

    public Task<IEnumerable<TResponse>> GetAllAsync();

    /// <summary>
    /// Updates an entity with the given id and the given values
    /// </summary>
    /// <exception cref="ArgumentNullException">entity is null</exception>
    /// <exception cref="ValidationException">Occurs if the entity does not pass validation</exception>
    /// <exception cref="ResourceNotFoundException">Occurs if an entity with the specified ID does not exist.
    /// See <see cref="IRepository{TKey,TEntity}.UpdateAsync"/> for the possible source of the exception.</exception>
    /// See <see cref="IRepository{TKey,TEntity}.UpdateAsync"/> for the possible source of the exception.</exception>
    public Task<TResponse> UpdateAsync(TRequest dto);

    /// <summary>
    /// Delete an entity with the given id
    /// </summary>
    /// <exception cref="ResourceNotFoundException">Occurs if an entity with the specified ID does not exist
    /// See <see cref="IRepository{TKey,TEntity}.DeleteAsync"/> for the possible source of the exception.</exception>
    public Task DeleteAsync(TKey id);
}