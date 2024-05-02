using LR2.Dto.Request.CreateTo;
using LR2.Dto.Request.UpdateTo;
using LR2.Dto.Response;

namespace LR2.Services.Interfaces;

public interface IIssueService
{
    Task<IssueResponseTo> GetIssueById(long id);
    Task<IEnumerable<IssueResponseTo>> GetIssues();
    Task<IssueResponseTo> CreateIssue(CreateIssueRequestTo createIssueRequestTo);
    Task DeleteIssue(long id);
    Task<IssueResponseTo> UpdateIssue(UpdateIssueRequestTo modifiedIssue);
}