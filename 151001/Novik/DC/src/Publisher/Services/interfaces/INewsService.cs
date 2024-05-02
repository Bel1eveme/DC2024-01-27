using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;

namespace Publisher.Services.interfaces;

public interface INewsService : IService<NewsResponseTo, NewsRequestTo>
{
    Task<IEnumerable<NewsResponseTo>> GetAllAsync();
    
    Task<NewsResponseTo?>? GetByIdAsync(long id);
    
    Task<NewsResponseTo> AddAsync(NewsRequestTo tEntityReq);
    
    Task<NewsResponseTo> UpdateAsync(NewsRequestTo tEntityReq);
    
    Task DeleteAsync(long id);
}