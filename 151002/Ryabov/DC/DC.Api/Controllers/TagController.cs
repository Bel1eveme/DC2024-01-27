using Forum.Api.Models.Dto;
using Forum.Api.Services;
using Microsoft.AspNetCore.Mvc;

namespace Forum.Api.Controllers;

[Route("api/v1.0/tags")]
[ApiController]
public class TagController : ControllerBase
{
    private readonly ITagService _tagService;

    public TagController(ITagService tagService)
    {
        _tagService = tagService;
    }

    [HttpGet]
    public async Task<IActionResult> Get()
    {
        return Ok(await _tagService.GetAllTags());
    }
    
    [HttpGet("{id:long}")]
    public async Task<IActionResult> Get(long id)
    {
        var tagResponseDto = await _tagService.GetTag(id);

        return tagResponseDto is not null ? Ok(tagResponseDto) : Problem(statusCode: 404);
    }
    
    [HttpPost]
    public async Task<IActionResult> Post([FromBody] TagRequestDto tagRequestDto)
    {
        var tagResponseDto = await _tagService.CreateTag(tagRequestDto);

        return Created(Request.Path + "/" + tagResponseDto.Id, tagResponseDto);
    }
    
    [HttpPut]
    public async Task<IActionResult> Put([FromBody] TagRequestDto tagRequestDto)
    {
        var tagResponseDto = await _tagService.UpdateTag(tagRequestDto);

        return tagResponseDto is not null ? Ok(tagResponseDto) : Problem(statusCode: 404);
    }
    
    [HttpDelete("{id:long}")]
    public async Task<IActionResult> Delete(long id)
    {
        var tagResponseDto = await _tagService.DeleteTag(id);

        return tagResponseDto is not null ? NoContent() : Problem(statusCode: 404);
    }
    
    
}