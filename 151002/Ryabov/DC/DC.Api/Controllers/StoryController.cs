using Forum.Api.Models.Dto;
using Forum.Api.Services;
using Microsoft.AspNetCore.Mvc;

namespace Forum.Api.Controllers;

[Route("api/v1.0/storys")]
[ApiController]
public class StoryController : ControllerBase
{
    private readonly IStoryService _storyService;

    public StoryController(IStoryService storyService)
    {
        _storyService = storyService;
    }

    [HttpGet]
    public async Task<IActionResult> Get()
    {
        return Ok(await _storyService.GetAllStories());
    }
    
    [HttpGet("{id:long}")]
    public async Task<IActionResult> Get(long id)
    {
        var storyResponseDto = await _storyService.GetStory(id);

        return storyResponseDto is not null ? Ok(storyResponseDto) : Problem(statusCode: 404);
    }
    
    [HttpPost]
    public async Task<IActionResult> Post([FromBody] StoryRequestDto storyRequestDto)
    {
        var storyResponseDto = await _storyService.CreateStory(storyRequestDto);

        return Created(Request.Path + "/" + storyResponseDto.Id, storyResponseDto);
    }
    
    [HttpPut]
    public async Task<IActionResult> Put([FromBody] StoryRequestDto storyRequestDto)
    {
        var storyResponseDto = await _storyService.UpdateStory(storyRequestDto);

        return storyResponseDto is not null ? Ok(storyResponseDto) : Problem(statusCode: 404);
    }
    
    [HttpDelete("{id:long}")]
    public async Task<IActionResult> Delete(long id)
    {
        var storyResponseDto = await _storyService.DeleteStory(id);

        return storyResponseDto is not null ? NoContent() : Problem(statusCode: 404);
    }
    
    [HttpGet("{id:long}/creator")]
    public async Task<IActionResult> GetCreatorByStory(long id)
    {
        var storyResponseDto = await _storyService.GetCreatorByStory(id);

        return storyResponseDto is not null ? Ok(storyResponseDto) : Problem(statusCode: 404);
    }
    
    [HttpGet("{id:long}/tags")]
    public async Task<IActionResult> GetTagsByStory(long id)
    {
        var storyResponseDto = await _storyService.GetTagsByStory(id);

        return Ok(storyResponseDto);
    }
    
    [HttpGet("{id:long}/posts")]
    public async Task<IActionResult> GetPostsByStory(long id)
    {
        var storyResponseDto = await _storyService.GetPostsByStory(id);

        return Ok(storyResponseDto);
    }
    
    [HttpPut("withFilers")]
    public async Task<IActionResult> Put([FromBody] StorySearchDto storySearchDto)
    {
        var storyResponseDto = await _storyService.GetStoriesWithFiltering(storySearchDto);

        return Ok(storyResponseDto);
    }
}