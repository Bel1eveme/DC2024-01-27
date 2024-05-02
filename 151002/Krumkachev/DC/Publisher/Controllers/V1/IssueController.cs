using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using Publisher.DTO.RequestDTO;
using Publisher.Services.Interfaces;

namespace Publisher.Controllers.V1;

[Route("api/v{version:apiVersion}/issue")]
[ApiController]
[ApiVersion("1.0")]
public class IssueController(IIssueService issueService) : ControllerBase
{
	[HttpGet]
	public async Task<IActionResult> GetIssue()
	{
		var issue = await issueService.GetIssueAsync();
		return Ok(issue);
	}

	[HttpGet("{id}")]
	public async Task<IActionResult> GetIssueById(long id)
	{
		var issue = await issueService.GetIssueByIdAsync(id);
		return Ok(issue);
	}

	[HttpPost]
	public async Task<IActionResult> CreateIssue([FromBody] IssueRequestDto issue)
	{
		var createdIssue = await issueService.CreateIssueAsync(issue);
		return CreatedAtAction(nameof(GetIssueById), new { id = createdIssue.Id }, createdIssue);
	}

	[HttpPut]
	public async Task<IActionResult> UpdateIssue([FromBody] IssueRequestDto issue)
	{
		var updatedIssue = await issueService.UpdateIssueAsync(issue);
		return Ok(updatedIssue);
	}

	[HttpDelete("{id}")]
	public async Task<IActionResult> DeleteIssue(long id)
	{
		await issueService.DeleteIssueAsync(id);
		return NoContent();
	}
}