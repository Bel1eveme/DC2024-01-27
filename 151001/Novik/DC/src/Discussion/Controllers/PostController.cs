using System.Net;
using Discussion.Models.DTOs.Requests;
using Discussion.Models.DTOs.Responses;
using Discussion.Services.interfaces;
using Microsoft.AspNetCore.Mvc;

namespace Discussion.Controllers;


[ApiController]
[Route("api/v1.0/[controller]")]
public class PostsController(IPostService postService) : ControllerBase
{
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
        var post = await postService.AddAsync(dto,dto.country);

        return CreatedAtAction(null, post);
    }

    [HttpGet]
    [ProducesResponseType(typeof(List<PostResponseTo>), (int)HttpStatusCode.OK)]
    public async Task<IActionResult> GetAll()
    {
        var posts = await postService.GetAllAsync();

        return Ok(posts);
    }

    [HttpGet("{id:long}")]
    [ProducesResponseType(typeof(PostResponseTo), (int)HttpStatusCode.OK)]
    [ProducesResponseType((int)HttpStatusCode.NotFound)]
    public async Task<IActionResult> GetById(long id)
    {
        var post = await postService.GetByIdAsync(id);
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
        var post = await postService.UpdateAsync(dto,dto.country);

        return Ok(post);
    }

    [HttpDelete("{id:long}")]
    [ProducesResponseType((int)HttpStatusCode.NoContent)]
    [ProducesResponseType((int)HttpStatusCode.NotFound)]
    public async Task<IActionResult> Delete(long id)
    {
        await postService.DeleteAsync(id);
        return NoContent();
    }
}