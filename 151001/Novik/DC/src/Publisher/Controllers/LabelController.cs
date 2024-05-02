using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Services.interfaces;

namespace Publisher.Controllers;

[Route("api/v1.0/[controller]")]
[ApiController]
public class LabelsController : ControllerBase
{
    private readonly ILabelService _labelService;

    public LabelsController(ILabelService labelService)
    {
        _labelService = labelService;
    }
    // GET: api/v1.0/User
    [HttpGet]
    public async Task<ActionResult<IEnumerable<LabelResponseTo>>> GetAllLabels()
    {
        var users = await _labelService.GetAllAsync();
        /*if (!users.Any())
        {
            return NotFound(new { Error = "Entity not found" });
        }*/
        return Ok(users);
    }

    // GET: api/v1.0/User/5
    [HttpGet("{id}")]
    public async Task<ActionResult<LabelResponseTo>> GetById([FromRoute]int id)
    {
        var user = await _labelService.GetByIdAsync(id);
        if (user == null)
        {
            return NotFound();
        }
        return Ok(user);
        /*try
        {
            var user = await _labelService.GetLabelByIdAsync(id);
            return Ok(user);
        }
        catch (Exception ex)
        {
            return NotFound(new { Error = "Entity not found" });
        }*/
    }

    // POST: api/v1.0/User
    [HttpPost]
    public async Task<ActionResult<LabelResponseTo>> CreateLabel(LabelRequestTo label)
    {
        if (!ModelState.IsValid)
        {
            return BadRequest(ModelState);
        }

        try
        {
            var labelAdded = await _labelService.AddAsync(label);
            return CreatedAtAction(nameof(GetById), new { id =labelAdded.id  }, labelAdded);
        }
        catch (DbUpdateException ex)
        {
            return StatusCode(403,new { Error = "IDK" });
        }
        catch (Exception e)
        {
            return BadRequest();
        }
        
    }

    // PUT: api/v1.0/User/5
    [HttpPut]
    public async Task<ActionResult<LabelResponseTo>> UpdateLabel(LabelRequestTo label)
    {
        if (!ModelState.IsValid)
        {
            return BadRequest(ModelState);
        }
        try
        {
            var labelUpdate = await _labelService.UpdateAsync(label);
            return Ok(labelUpdate);
        }
        catch (Exception ex)
        {
            // Обработка ошибок, если обновление не удалось
            return NotFound(new { Error = "Entity not found" });
        }
    }

    // DELETE: api/v1.0/User/5
    [HttpDelete("{id}")]
    public async Task<ActionResult<LabelResponseTo>> DeleteLabel(int id)
    {
        try
        {
            await _labelService.DeleteAsync(id);
            return NoContent();
        }
        catch (Exception e)
        {
            return NotFound(new { Error = "Entity not found" });
        }
    }
}