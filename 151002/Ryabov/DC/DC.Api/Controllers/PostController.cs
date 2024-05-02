using Forum.Api.Models.Dto;
using Forum.Api.Services;
using Microsoft.AspNetCore.Mvc;

namespace Forum.Api.Controllers;

[Route("api/v1.0/posts")]
[ApiController]
public class PostController : ControllerBase
{
    private readonly IPostService _postService;

    public PostController(IPostService postService)
    {
        _postService = postService;
    }

    [HttpGet]
    public async Task<IActionResult> Get()
    {
        return Ok(await _postService.GetAllPosts());
    }
    
    [HttpGet("{id:long}")]
    public async Task<IActionResult> Get(long id)
    {
        var postResponseDto = await _postService.GetPost(id);

        return postResponseDto is not null ? Ok(postResponseDto) : Problem(statusCode: 404);
    }
    
    [HttpPost]
    public async Task<IActionResult> Post([FromBody] PostRequestDto postRequestDto)
    {
        var postResponseDto = await _postService.CreatePost(postRequestDto);

        return Created(Request.Path + "/" + postResponseDto.Id, postResponseDto);
    }
    
    [HttpPut]
    public async Task<IActionResult> Put([FromBody] PostRequestDto postRequestDto)
    {
        var postResponseDto = await _postService.UpdatePost(postRequestDto);

        return postResponseDto is not null ? Ok(postResponseDto) : Problem(statusCode: 404);
    }
    
    [HttpDelete("{id:long}")]
    public async Task<IActionResult> Delete(long id)
    {
        var postResponseDto = await _postService.DeletePost(id);

        return postResponseDto is not null ? NoContent() : Problem(statusCode: 404);
    }
    
    
}