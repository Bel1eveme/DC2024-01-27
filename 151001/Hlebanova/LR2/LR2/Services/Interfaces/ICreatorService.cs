using LR2.Dto.Request.CreateTo;
using LR2.Dto.Request.UpdateTo;
using LR2.Dto.Response;

namespace LR2.Services.Interfaces;

public interface ICreatorService
{
    Task<CreatorResponseTo> GetCreatorById(long id);
    Task<IEnumerable<CreatorResponseTo>> GetCreators();
    Task<CreatorResponseTo> CreateCreator(CreateCreatorRequestTo createCreatorRequestTo);
    Task DeleteCreator(long id);
    Task<CreatorResponseTo> UpdateCreator(UpdateCreatorRequestTo modifiedCreator);
}