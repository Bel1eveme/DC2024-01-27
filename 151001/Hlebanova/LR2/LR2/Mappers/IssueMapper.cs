using Riok.Mapperly.Abstractions;
using LR2.Dto.Request.CreateTo;
using LR2.Dto.Request.UpdateTo;
using LR2.Dto.Response;
using LR2.Models;

namespace LR2.Mappers;

[Mapper]
public static partial class IssueMapper
{
    public static partial Issue Map(UpdateIssueRequestTo updateIssueRequestTo);
    public static partial Issue Map(CreateIssueRequestTo createIssueRequestTo);
    public static partial IssueResponseTo Map(Issue issue);
    public static partial IEnumerable<IssueResponseTo> Map(IEnumerable<Issue> issues);

    public static partial IEnumerable<Issue> Map(
        IEnumerable<UpdateIssueRequestTo> issueRequestTos);
}