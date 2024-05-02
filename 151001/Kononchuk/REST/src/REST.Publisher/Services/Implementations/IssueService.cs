using AutoMapper;
using FluentValidation;
using REST.Publisher.Infrastructure.Redis.Interfaces;
using REST.Publisher.Models.DTOs.Request;
using REST.Publisher.Models.DTOs.Response;
using REST.Publisher.Models.Entities;
using REST.Publisher.Repositories.Interfaces;
using REST.Publisher.Services.Interfaces;
using Exceptions_ValidationException = REST.Publisher.Utilities.Exceptions.ValidationException;

namespace REST.Publisher.Services.Implementations;

public class IssueService(
    IMapper mapper,
    IIssueRepository<long> issueRepository,
    AbstractValidator<Issue> validator,
    ICacheService cacheService)
    : IIssueService
{
    private const string Prefix = "issue-";

    public async Task<IssueResponseDto> CreateAsync(IssueRequestDto dto)
    {
        if (dto == null) throw new ArgumentNullException(nameof(dto));
        var issue = mapper.Map<Issue>(dto);

        var validationResult = await validator.ValidateAsync(issue);

        if (!validationResult.IsValid)
        {
            throw new Exceptions_ValidationException("Issue data has not been validated", 40001);
        }

        var createdIssue = await issueRepository.AddAsync(issue);

        await cacheService.SetAsync(Prefix + createdIssue.Id, createdIssue);

        return mapper.Map<IssueResponseDto>(createdIssue);
    }

    public async Task<IssueResponseDto> GetByIdAsync(long id)
    {
        var foundIssue = await cacheService.GetAsync(Prefix + id,
            async () => await issueRepository.GetByIdAsync(id));

        return mapper.Map<IssueResponseDto>(foundIssue);
    }

    public async Task<IEnumerable<IssueResponseDto>> GetAllAsync()
    {
        var issues = (await issueRepository.GetAllAsync()).ToList();
        var result = new List<IssueResponseDto>(issues.Count);

        foreach (var issue in issues)
        {
            await cacheService.SetAsync(Prefix + issue.Id, issue);
            result.Add(mapper.Map<IssueResponseDto>(issue));
        }

        return result;
    }

    public async Task<IssueResponseDto> UpdateAsync(long id, IssueRequestDto dto)
    {
        if (dto == null) throw new ArgumentNullException(nameof(dto));
        var issue = mapper.Map<Issue>(dto);

        var validationResult = await validator.ValidateAsync(issue);

        if (!validationResult.IsValid)
        {
            throw new Exceptions_ValidationException("Issue data has not been validated", 40002);
        }

        var updatedIssue = await issueRepository.UpdateAsync(id, issue);

        await cacheService.SetAsync(Prefix + updatedIssue.Id, updatedIssue);

        return mapper.Map<IssueResponseDto>(updatedIssue);
    }

    public async Task DeleteAsync(long id)
    {
        await issueRepository.DeleteAsync(id);
        await cacheService.RemoveAsync(Prefix + id);
    }
}