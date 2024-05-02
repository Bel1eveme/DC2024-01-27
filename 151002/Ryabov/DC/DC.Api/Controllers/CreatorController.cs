using Forum.Api.Models.Dto;
using Forum.Api.Services;
using Microsoft.AspNetCore.Mvc;

namespace Forum.Api.Controllers;

[Route("api/v1.0/creators")]
[ApiController]
public class CreatorController : ControllerBase
{
    private readonly ICreatorService _creatorService;

    public CreatorController(ICreatorService creatorService)
    {
        _creatorService = creatorService;
    }

    [HttpGet]
    public async Task<IActionResult> Get()
    {
        return Ok(await _creatorService.GetAllCreators());
    }
    
    [HttpGet("{id:long}")]
    public async Task<IActionResult> Get(long id)
    {
        var creatorResponseDto = await _creatorService.GetCreator(id);

        return creatorResponseDto is not null ? Ok(creatorResponseDto) : Problem(statusCode: 404);
    }
    
    [HttpPost]
    public async Task<IActionResult> Post([FromBody] CreatorRequestDto creatorRequestDto)
    {
        var creatorResponseDto = await _creatorService.CreateCreator(creatorRequestDto);

        return Created(Request.Path + "/" + creatorResponseDto.Id, creatorResponseDto);
    }
    
    [HttpPut]
    public async Task<IActionResult> Put([FromBody] CreatorRequestDto creatorRequestDto)
    {
        var creatorResponseDto = await _creatorService.UpdateCreator(creatorRequestDto);

        return creatorResponseDto is not null ? Ok(creatorResponseDto) : Problem(statusCode: 404);
    }
    
    [HttpDelete("{id:long}")]
    public async Task<IActionResult> Delete(long id)
    {
        var creatorResponseDto = await _creatorService.DeleteCreator(id);

        return creatorResponseDto is not null ? NoContent() : Problem(statusCode: 404);
    }
    
    
}