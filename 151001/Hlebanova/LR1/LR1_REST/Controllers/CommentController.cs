using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using LR1.Dto.Request.CreateTo;
using LR1.Dto.Request.UpdateTo;
using LR1.Dto.Response;
using LR1.Models;
using LR1.Services.Interfaces;

namespace LR1.Controllers;

[Route("api/v{version:apiVersion}/Comments")]
[ApiVersion("1.0")]
[ApiController]
public class CommentController(ICommentService service) : ControllerBase
{
    [HttpGet("{id:long}")]
    public async Task<ActionResult<Comment>> GetComment(long id)
    {
        return Ok(await service.GetCommentById(id));
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<Comment>>> GetComments()
    {
        return Ok(await service.GetComments());
    }

    [HttpPost]
    public async Task<ActionResult<CommentResponseTo>> CreateComment(CreateCommentRequestTo createCommentRequestTo)
    {
        var addedComment = await service.CreateComment(createCommentRequestTo);
        return CreatedAtAction(nameof(GetComment), new { id = addedComment.Id }, addedComment);
    }

    [HttpDelete("{id:long}")]
    public async Task<IActionResult> DeleteComment(long id)
    {
        await service.DeleteComment(id);
        return NoContent();
    }

    [HttpPut]
    public async Task<ActionResult<CommentResponseTo>> UpdateComment(UpdateCommentRequestTo updateCommentRequestTo)
    {
        return Ok(await service.UpdateComment(updateCommentRequestTo));
    }
}