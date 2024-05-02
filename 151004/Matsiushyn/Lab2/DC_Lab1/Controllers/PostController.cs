using DC_Lab1.DTO;
using DC_Lab1.DTO.Interface;
using DC_Lab1.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace DC_Lab1.Controllers
{
    [ApiController]
    [Route("/api/v1.0/posts")]
    public class PostController(IPostService PostService) : Controller
    {
        [HttpGet]
        public JsonResult GetMessages()
        {
            try
            {
                var Posts = PostService.GetAllEnt();
                return Json(Posts);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());

            }

        }

        [HttpGet]
        [Route("{postId:int}")]
        public async Task<JsonResult> GetPostById(int PostId)
        {
            try
            {
                var Message = await PostService.GetEntById(PostId);
                return Json(Message);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }


        }

        [HttpPost]
        public async Task<JsonResult> CreateMessage(PostRequestTo PostTo)
        {
            try
            {
                Response.StatusCode = 201;
                var Post = await PostService.CreateEnt(PostTo);
                return Json(Post);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }

        }

        [HttpPut]
        public async Task<JsonResult> UpdatePost(PostRequestTo PostTo)
        {
            IResponseTo newPost;
            try
            {
                newPost = await PostService.UpdateEnt(PostTo);
                Response.StatusCode = 200;
                return Json(newPost);

            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }
        }

        [HttpDelete("{postId}")]
        public async Task<IActionResult> DeletePost(int postId)
        {
            try
            {
                Response.StatusCode = 204;
                await PostService.DeleteEnt(postId);
            }
            catch
            {
                Response.StatusCode = 401;
                return BadRequest();
            }

            return NoContent();



        }
    }
}
