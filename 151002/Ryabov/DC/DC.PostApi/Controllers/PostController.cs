using Forum.PostApi.Models.Dto;
using Forum.PostApi.Services;
using Microsoft.AspNetCore.Mvc;

namespace Forum.PostApi.Controllers;

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
        return Ok(await _postService.GetAllPostsAsync());
    }
    
    [HttpGet("{id:long}")]
    public async Task<IActionResult> Get(long id)
    {
        var postResponseDto = await _postService.GetPostAsync(id);

        return postResponseDto is not null ? Ok(postResponseDto) : Problem(statusCode: 404);
    }
    
    [HttpPost]
    public async Task<IActionResult> Post([FromBody] PostRequestDto postRequestDto)
    {
        var postResponseDto = await _postService.CreatePostAsync(postRequestDto);

        return postResponseDto is not null ? Created(Request.Path + "/" + postResponseDto.Id, postResponseDto) : Problem(statusCode: 404);
    }
    
    [HttpPut]
    public async Task<IActionResult> Put([FromBody] PostRequestDto postRequestDto)
    {
        var postResponseDto = await _postService.UpdatePostAsync(postRequestDto);

        return postResponseDto is not null ? Ok(postResponseDto) : Problem(statusCode: 404);
    }
    
    [HttpDelete("{id:long}")]
    public async Task<IActionResult> Delete(long id)
    {
        var postResponseDto = await _postService.DeletePostAsync(id);

        return postResponseDto is not null ? NoContent() : Problem(statusCode: 404);
    }
    
    
}