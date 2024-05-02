using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using LR2.Dto.Request.CreateTo;
using LR2.Dto.Request.UpdateTo;
using LR2.Dto.Response;
using LR2.Models;
using LR2.Services.Interfaces;

namespace LR2.Controllers;

[Route("api/v{version:apiVersion}/labels")]
[ApiVersion("1.0")]
[ApiController]
public class LabelController(ILabelService service) : ControllerBase
{
    [HttpGet("{id:long}")]
    public async Task<ActionResult<Label>> GetLabel(long id)
    {
        return Ok(await service.GetLabelById(id));
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<Label>>> GetLabels()
    {
        return Ok(await service.GetLabels());
    }

    [HttpPost]
    public async Task<ActionResult<LabelResponseTo>> CreateLabel(CreateLabelRequestTo createLabelRequestTo)
    {
        var addedLabel = await service.CreateLabel(createLabelRequestTo);
        return CreatedAtAction(nameof(GetLabel), new { id = addedLabel.Id }, addedLabel);
    }

    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteLabel(long id)
    {
        await service.DeleteLabel(id);
        return NoContent();
    }

    [HttpPut]
    public async Task<ActionResult<LabelResponseTo>> UpdateLabel(UpdateLabelRequestTo updateLabelRequestTo)
    {
        return Ok(await service.UpdateLabel(updateLabelRequestTo));
    }
}