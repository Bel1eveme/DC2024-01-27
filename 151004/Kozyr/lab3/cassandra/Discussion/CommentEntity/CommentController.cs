using AutoMapper;
using Discussion.Common;
using Discussion.CommentEntity.Interface;
using Microsoft.AspNetCore.Mvc;

namespace Discussion.CommentEntity
{
    [Route("api/v1.0/comments")]
    [ApiController]
    public class CommentController(ICommentService CommentService, ILogger<CommentController> Logger, IMapper Mapper)
        : AbstractController<Comment, CommentRequestTO, CommentResponseTO>(CommentService, Logger, Mapper)
    { }
}
