using LR1.Dto.Request.CreateTo;
using LR1.Dto.Request.UpdateTo;
using LR1.Dto.Response;

namespace LR1.Services.Interfaces;

public interface IIssueService
{
    Task<IssueResponseTo> GetIssueById(long id);
    Task<IEnumerable<IssueResponseTo>> GetIssues();
    Task<IssueResponseTo> CreateIssue(CreateIssueRequestTo createIssueRequestTo);
    Task DeleteIssue(long id);
    Task<IssueResponseTo> UpdateIssue(UpdateIssueRequestTo modifiedIssue);
}