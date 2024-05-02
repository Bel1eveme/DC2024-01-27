namespace Discussion.Services.interfaces;

public interface IService<TEntityResponse,TEntityRequest>
{
    Task<IEnumerable<TEntityResponse>> GetAllAsync();
    
    Task<TEntityResponse?>? GetByIdAsync(long id);
    
    Task<TEntityResponse> AddAsync(TEntityRequest tEntityReq, String country);
    
    Task<TEntityResponse> UpdateAsync(TEntityRequest tEntityReq, string country);
    
    Task DeleteAsync(long id);

}