using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Services.interfaces;

namespace Publisher.Controllers;

[Route("api/v1.0/[controller]")]
[ApiController]
public class NewsController : ControllerBase
{
    
    private readonly INewsService _newsService;

    public NewsController(INewsService newsService)
    {
        _newsService = newsService;
    }
    // GET: api/v1.0/User
    [HttpGet]
    public async Task<ActionResult<IEnumerable<NewsResponseTo>>> GetAllNews()
    {
        var news = await _newsService.GetAllAsync();
        /*if (!users.Any())
        {
            return NotFound(new { Error = "Entity not found" });
        }*/
        return Ok(news);
    }

    // GET: api/v1.0/User/5
    [HttpGet("{id}")]
    public async Task<ActionResult<NewsResponseTo>> GetNewsById([FromRoute]long id)
    {
        var news = await _newsService.GetByIdAsync(id);
        return Ok(news);
        /*try
        {
            var user = await _newsService.GetNewsByIdAsync(id);
            return Ok(user);
        }
        catch (Exception ex)
        {
            return NotFound(new { Error = "Entity not found" });
            
        }*/


    }

    // POST: api/v1.0/User
    [HttpPost]
    public async Task<ActionResult<NewsResponseTo>> CreateNews([FromBody]NewsRequestTo news)
    {
        if (!ModelState.IsValid)
        {
            return BadRequest(ModelState);
        }

        try
        {
            var newsAdded = await _newsService.AddAsync(news);
            return CreatedAtAction(nameof(GetNewsById), new { id = newsAdded.id  }, newsAdded);
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
    public async Task<ActionResult<NewsResponseTo>> UpdateNews(NewsRequestTo news)
    {
        if (!ModelState.IsValid)
        {
            return BadRequest(ModelState);
        }
        try
        {
            var newsUpdate = await _newsService.UpdateAsync(news);
            return Ok(newsUpdate);
        }
        catch (Exception ex)
        {
            // Обработка ошибок, если обновление не удалось
            return NotFound(new { Error = "Entity not found" });
        }
        
    }

    // DELETE: api/v1.0/User/5
    [HttpDelete("{id}")]
    public async Task<ActionResult<NewsResponseTo>> DeleteUser(int id)
    {
        try
        {
            await _newsService.DeleteAsync(id);
            return NoContent();
        }
        catch (Exception e)
        {
            return NotFound(new { Error = "Entity not found" });
        }
        
    }
}