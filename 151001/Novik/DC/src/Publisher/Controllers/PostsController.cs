using System.Net;
using System.Text;
using Discussion.Models.DTOs.Requests;
using Discussion.Models.DTOs.Responses;
using Microsoft.AspNetCore.Mvc;
using Publisher.Services.interfaces;


namespace Publisher.Controllers;

[Route("api/v1.0/[controller]")]
[ApiController]
public class PostsController : ControllerBase
{
    private readonly IPostService _postService;

    public PostsController(IPostService postService)
    {
        _postService = postService;
    }
    
    private string GetCountryCode()
    {
        var languages = HttpContext.Request.Headers.AcceptLanguage.FirstOrDefault();

        if (languages is null)
        {
            return "us";
        }

        var country = languages
            .Split(",").First()
            .Split(";").First()
            .Split("-").Last();
        return country.ToLower();
    }

    [HttpPost]
    [ProducesResponseType(typeof(PostResponseTo), (int)HttpStatusCode.Created)]
    public async Task<IActionResult> Create([FromBody] PostRequestTo dto)
    {
        dto.country = GetCountryCode();

        var post = await _postService.AddAsync(dto);

        return CreatedAtAction(null, post);
    }

    [HttpGet]
    [ProducesResponseType(typeof(List<PostResponseTo>), (int)HttpStatusCode.OK)]
    public async Task<IActionResult> GetAll()
    {
        var posts = await _postService.GetAllAsync();

        return Ok(posts);
    }

    [HttpGet("{id:long}")]
    [ProducesResponseType(typeof(PostResponseTo), (int)HttpStatusCode.OK)]
    [ProducesResponseType((int)HttpStatusCode.NotFound)]
    public async Task<IActionResult> GetById(long id)
    {
        var post = await _postService.GetByIdAsync(id);
        if (post == null)
        {
            return NotFound();
        }
        return Ok(post);
    }

    [HttpPut]
    [ProducesResponseType(typeof(PostResponseTo), (int)HttpStatusCode.OK)]
    [ProducesResponseType((int)HttpStatusCode.NotFound)]
    public async Task<IActionResult> Update([FromBody] PostRequestTo dto)
    {
        dto.country = GetCountryCode();
        var post = await _postService.UpdateAsync(dto);
        return Ok(post);
    }

    [HttpDelete("{id:long}")]
    [ProducesResponseType((int)HttpStatusCode.NoContent)]
    [ProducesResponseType((int)HttpStatusCode.NotFound)]
    public async Task<IActionResult> Delete(long id)
    {
        await _postService.DeleteAsync(id);
        return NoContent();
    }
}