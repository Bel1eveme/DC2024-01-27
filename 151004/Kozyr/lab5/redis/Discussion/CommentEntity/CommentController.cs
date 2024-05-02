using AutoMapper;
using Discussion.Common;
using Discussion.CommentEntity.Dto;
using Discussion.CommentEntity.Interface;
using Microsoft.AspNetCore.Mvc;
using System.Net;

namespace Discussion.CommentEntity
{
    [Route("api/v1.0/comments")]
    [ApiController]
    public class CommentController(ICommentService CommentService, ILogger<CommentController> Logger, IMapper Mapper)
        : AbstractController<Comment, CommentRequestTO, CommentResponseTO>(CommentService, Logger, Mapper)
    {
        [HttpGet]
        [Route("{id:int}")]
        public override async Task<JsonResult> GetByID([FromRoute] int id)
        {
            Logger.LogInformation("Get {type} {id}", typeof(Comment), id);
            var json = Json(null);
            json.StatusCode = (int)HttpStatusCode.OK;

            try
            {
                var response = await CommentService.GetByID(id);
                json.Value = response;
            }
            catch
            {
                Logger.LogError("ERROR getting {type} {id}", typeof(Comment), id);
                json.Value = new CommentResponseTO(default, default, string.Empty, string.Empty);
                json.StatusCode = (int)HttpStatusCode.NotFound;
            }

            return json;
        }
    }
}
