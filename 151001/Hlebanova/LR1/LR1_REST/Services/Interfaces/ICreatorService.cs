using LR1.Dto.Request.CreateTo;
using LR1.Dto.Request.UpdateTo;
using LR1.Dto.Response;

namespace LR1.Services.Interfaces;

public interface ICreatorService
{
    Task<CreatorResponseTo> GetCreatorById(long id);
    Task<IEnumerable<CreatorResponseTo>> GetCreators();
    Task<CreatorResponseTo> CreateCreator(CreateCreatorRequestTo createCreatorRequestTo);
    Task DeleteCreator(long id);
    Task<CreatorResponseTo> UpdateCreator(UpdateCreatorRequestTo modifiedCreator);
}