using AutoMapper;
using FluentValidation;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.Exceptions;
using Publisher.Infrastructure.Validators;
using Publisher.Models;
using Publisher.Repositories.Interfaces;
using Publisher.Services.Interfaces;

namespace Publisher.Services.Implementations;

public class IssueService(
	IIssueRepository issueRepository,
	ICreatorRepository creatorRepository,
	IMapper mapper,
	IssueRequestDtoValidator validator) : IIssueService
{
	private readonly ICreatorRepository _creatorRepository = creatorRepository;
	private readonly IMapper _mapper = mapper;
	private readonly IIssueRepository _issueRepository = issueRepository;
	private readonly IssueRequestDtoValidator _validator = validator;

	public async Task<IEnumerable<IssueResponseDto>> GetIssueAsync()
	{
		var issue = await _issueRepository.GetAllAsync();
		return _mapper.Map<IEnumerable<IssueResponseDto>>(issue);
	}

	public async Task<IssueResponseDto> GetIssueByIdAsync(long id)
	{
		var issue = await _issueRepository.GetByIdAsync(id)
				   ?? throw new NotFoundException(ErrorMessages.IssueNotFoundMessage(id));
		return _mapper.Map<IssueResponseDto>(issue);
	}

	public async Task<IssueResponseDto> CreateIssueAsync(IssueRequestDto issue)
	{
		_validator.ValidateAndThrow(issue);

		var issueToCreate = _mapper.Map<Issue>(issue);
		issueToCreate.CreatorId = issue.CreatorId;
		issueToCreate.Created = DateTime.UtcNow;
		issueToCreate.Modified = DateTime.UtcNow;

		var createdIssue = await _issueRepository.CreateAsync(issueToCreate);
		var responseDto = _mapper.Map<IssueResponseDto>(createdIssue);
		return responseDto;
	}

	public async Task<IssueResponseDto> UpdateIssueAsync(IssueRequestDto issue)
	{
		_validator.ValidateAndThrow(issue);
		var issueToUpdate = _mapper.Map<Issue>(issue);
		issueToUpdate.Modified = DateTime.UtcNow;
		var updatedIssue = await _issueRepository.UpdateAsync(issueToUpdate)
						  ?? throw new NotFoundException(ErrorMessages.IssueNotFoundMessage(issue.Id));
		return _mapper.Map<IssueResponseDto>(updatedIssue);
	}

	public async Task DeleteIssueAsync(long id)
	{
		if (!await _issueRepository.DeleteAsync(id)) throw new NotFoundException(ErrorMessages.IssueNotFoundMessage(id));
	}
}