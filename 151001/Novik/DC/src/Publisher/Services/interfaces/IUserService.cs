using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;

namespace Publisher.Services.interfaces;

public interface IUserService : IService<UserResponseTo,UserRequestTo>
{
    Task<IEnumerable<UserResponseTo>> GetAllAsync();
    
    Task<UserResponseTo?>? GetByIdAsync(long id);
    
    Task<UserResponseTo> AddAsync(UserRequestTo tEntityReq);
    
    Task<UserResponseTo> UpdateAsync(UserRequestTo tEntityReq);
    
    Task DeleteAsync(long id);
}