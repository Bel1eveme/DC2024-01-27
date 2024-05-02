using AutoMapper;
using Discussion.Common;
using Discussion.PostEntity.Dto;
using Discussion.PostEntity.Interface;
using Microsoft.AspNetCore.Mvc;
using System.Net;

namespace Discussion.PostEntity
{
    [Route("api/v1.0/posts")]
    [ApiController]
    public class PostController(IPostService PostService, ILogger<PostController> Logger, IMapper Mapper)
        : AbstractController<Post, PostRequestTO, PostResponseTO>(PostService, Logger, Mapper)
    {
        [HttpGet]
        [Route("{id:int}")]
        public override async Task<JsonResult> GetByID([FromRoute] int id)
        {
            Logger.LogInformation("Get {type} {id}", typeof(Post), id);
            var json = Json(null);
            json.StatusCode = (int)HttpStatusCode.OK;

            try
            {
                var response = await PostService.GetByID(id);
                json.Value = response;
            }
            catch
            {
                Logger.LogError("ERROR getting {type} {id}", typeof(Post), id);
                json.Value = new PostResponseTO(default, default, string.Empty, string.Empty);
                json.StatusCode = (int)HttpStatusCode.NotFound;
            }

            return json;
        }
    }
}
