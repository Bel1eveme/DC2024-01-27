using Riok.Mapperly.Abstractions;
using LR1.Dto.Request.CreateTo;
using LR1.Dto.Request.UpdateTo;
using LR1.Dto.Response;
using LR1.Models;

namespace LR1.Mappers;

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