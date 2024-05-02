using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using Publisher.DTO.RequestDTO;
using Publisher.Services.Interfaces;

namespace Publisher.Controllers.V1;

[Route("api/v{version:apiVersion}/labels")]
[ApiController]
[ApiVersion("1.0")]
public class LabelController(ILabelService labelService) : ControllerBase
{
	[HttpGet]
	public async Task<IActionResult> GetLabels()
	{
		var labels = await labelService.GetLabelsAsync();
		return Ok(labels);
	}

	[HttpGet("{id}")]
	public async Task<IActionResult> GetLabelById(long id)
	{
		var label = await labelService.GetLabelByIdAsync(id);
		return Ok(label);
	}

	[HttpPost]
	public async Task<IActionResult> CreateLabel([FromBody] LabelRequestDto label)
	{
		var createdLabel = await labelService.CreateLabelAsync(label);
		return CreatedAtAction(nameof(GetLabelById), new { id = createdLabel.Id }, createdLabel);
	}

	[HttpPut]
	public async Task<IActionResult> UpdateLabel([FromBody] LabelRequestDto label)
	{
		var updatedLabel = await labelService.UpdateLabelAsync(label);
		return Ok(updatedLabel);
	}

	[HttpDelete("{id}")]
	public async Task<IActionResult> DeleteLabel(long id)
	{
		await labelService.DeleteLabelAsync(id);
		return NoContent();
	}
}