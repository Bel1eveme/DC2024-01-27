namespace Publisher.Services.interfaces;

public interface IService<TEntityResponse,TEntityRequest>
{
    Task<IEnumerable<TEntityResponse>> GetAllAsync();
    
    Task<TEntityResponse?>? GetByIdAsync(long id);
    
    Task<TEntityResponse> AddAsync(TEntityRequest tEntityReq);
    
    Task<TEntityResponse> UpdateAsync(TEntityRequest tEntityReq);
    
    Task DeleteAsync(long id);

}