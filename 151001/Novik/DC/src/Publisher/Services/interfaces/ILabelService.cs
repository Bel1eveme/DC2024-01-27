using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;

namespace Publisher.Services.interfaces;

public interface ILabelService : IService<LabelResponseTo, LabelRequestTo>
{
    Task<IEnumerable<LabelResponseTo>> GetAllAsync();
    
    Task<LabelResponseTo?>? GetByIdAsync(long id);
    
    Task<LabelResponseTo> AddAsync(LabelRequestTo tEntityReq);
    
    Task<LabelResponseTo> UpdateAsync(LabelRequestTo tEntityReq);
    
    Task DeleteAsync(long id);
}