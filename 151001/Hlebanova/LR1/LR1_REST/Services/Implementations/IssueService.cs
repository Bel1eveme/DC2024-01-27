using Microsoft.EntityFrameworkCore;
using LR1.Dto.Request.CreateTo;
using LR1.Dto.Request.UpdateTo;
using LR1.Dto.Response;
using LR1.Exceptions;
using LR1.Services.Interfaces;
using LR1.Storage;
using IssueMapper = LR1.Mappers.IssueMapper;

namespace LR1.Services.Implementations;

public sealed class IssueService(AppDbContext context) : IIssueService
{
    public async Task<IssueResponseTo> GetIssueById(long id)
    {
        var Issue = await context.Issues.FindAsync(id);
        if (Issue == null) throw new EntityNotFoundException($"Issue with id = {id} doesn't exist.");

        return IssueMapper.Map(Issue);
    }

    public async Task<IEnumerable<IssueResponseTo>> GetIssues()
    {
        return IssueMapper.Map(await context.Issues.ToListAsync());
    }

    public async Task<IssueResponseTo> CreateIssue(CreateIssueRequestTo createIssueRequestTo)
    {
        var Issue = IssueMapper.Map(createIssueRequestTo);
        await context.Issues.AddAsync(Issue);
        await context.SaveChangesAsync();
        return IssueMapper.Map(Issue);
    }

    public async Task DeleteIssue(long id)
    {
        var Issue = await context.Issues.FindAsync(id);
        if (Issue == null) throw new EntityNotFoundException($"Issue with id = {id} doesn't exist.");

        context.Issues.Remove(Issue);
        await context.SaveChangesAsync();
    }

    public async Task<IssueResponseTo> UpdateIssue(UpdateIssueRequestTo modifiedIssue)
    {
        var Issue = await context.Issues.FindAsync(modifiedIssue.Id);
        if (Issue == null) throw new EntityNotFoundException($"Issue with id = {modifiedIssue.Id} doesn't exist.");

        context.Entry(Issue).State = EntityState.Modified;

        Issue.Id = modifiedIssue.Id;
        Issue.Content = modifiedIssue.Content;
        Issue.CreatorId = modifiedIssue.CreatorId;
        Issue.Title = modifiedIssue.Title;


        await context.SaveChangesAsync();
        return IssueMapper.Map(Issue);
    }
}