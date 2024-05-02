using Discussion.Models.DTOs.Requests;
using Discussion.Models.DTOs.Responses;

namespace Discussion.Services.interfaces;

public interface IPostService : IService<PostResponseTo, PostRequestTo>
{
    Task<IEnumerable<PostResponseTo>> GetAllAsync();
    
    Task<PostResponseTo?>? GetByIdAsync(long id);
    
    Task<PostResponseTo> AddAsync(PostRequestTo tEntityReq, string country);
    
    Task<PostResponseTo> UpdateAsync(PostRequestTo tEntityReq, string country);
    
    Task DeleteAsync(long id);
}