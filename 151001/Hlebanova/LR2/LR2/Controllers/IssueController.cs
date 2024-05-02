using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using LR2.Dto.Request.CreateTo;
using LR2.Dto.Request.UpdateTo;
using LR2.Dto.Response;
using LR2.Models;
using LR2.Services.Interfaces;

namespace LR2.Controllers;

[Route("api/v{version:apiVersion}/issues")]
[ApiVersion("1.0")]
[ApiController]
public class IssueController(IIssueService service) : ControllerBase
{
    [HttpGet("{id:long}")]
    public async Task<ActionResult<Issue>> GetIssue(long id)
    {
        return Ok(await service.GetIssueById(id));
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<Issue>>> GetIssues()
    {
        return Ok(await service.GetIssues());
    }

    [HttpPost]
    public async Task<ActionResult<IssueResponseTo>> CreateIssue(CreateIssueRequestTo createIssueRequestTo)
    {
        var addedIssue = await service.CreateIssue(createIssueRequestTo);
        return CreatedAtAction(nameof(GetIssue), new { id = addedIssue.Id }, addedIssue);
    }

    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteIssue(long id)
    {
        await service.DeleteIssue(id);
        return NoContent();
    }

    [HttpPut]
    public async Task<ActionResult<IssueResponseTo>> UpdateIssue(UpdateIssueRequestTo updateIssueRequestTo)
    {
        return Ok(await service.UpdateIssue(updateIssueRequestTo));
    }
}