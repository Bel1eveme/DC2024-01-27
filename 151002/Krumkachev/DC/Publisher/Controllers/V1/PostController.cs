using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using Publisher.DTO.RequestDTO;
using Publisher.HttpClients.Interfaces;
using Publisher.Services.Interfaces;

namespace Publisher.Controllers.V1;

[Route("api/v{version:apiVersion}/notes")]
[ApiController]
[ApiVersion("1.0")]
public class PostController(IDiscussionClient noteClient, IIssueService issueService) : ControllerBase
{
	[HttpGet]
	public async Task<IActionResult> GetNotes()
	{
		var notes = await noteClient.GetNotesAsync();
		return Ok(notes);
	}

	[HttpGet("{id}")]
	public async Task<IActionResult> GetNoteById(long id)
	{
		var note = await noteClient.GetNoteByIdAsync(id);
		return Ok(note);
	}

	[HttpPost]
	public async Task<IActionResult> CreateNote([FromBody] PostRequestDto note)
	{
		var issue = await issueService.GetIssueByIdAsync(note.IssueId);
		var createdNote = await noteClient.CreateNoteAsync(note);
		return CreatedAtAction(nameof(GetNoteById), new { id = createdNote.Id }, createdNote);
	}

	[HttpPut]
	public async Task<IActionResult> UpdateNote([FromBody] PostRequestDto note)
	{
		var updatedNote = await noteClient.UpdateNoteAsync(note);
		return Ok(updatedNote);
	}

	[HttpDelete("{id}")]
	public async Task<IActionResult> DeleteNote(long id)
	{
		await noteClient.DeleteNoteAsync(id);
		return NoContent();
	}
}