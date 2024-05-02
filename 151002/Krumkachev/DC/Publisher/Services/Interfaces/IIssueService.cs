using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.Services.Interfaces;

public interface IIssueService
{
	Task<IEnumerable<IssueResponseDto>> GetIssueAsync();

	Task<IssueResponseDto> GetIssueByIdAsync(long id);

	Task<IssueResponseDto> CreateIssueAsync(IssueRequestDto issue);

	Task<IssueResponseDto> UpdateIssueAsync(IssueRequestDto issue);

	Task DeleteIssueAsync(long id);
}