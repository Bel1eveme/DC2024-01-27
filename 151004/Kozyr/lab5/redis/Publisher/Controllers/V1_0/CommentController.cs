using Microsoft.AspNetCore.Mvc;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Kafka.Model;
using Publisher.Service.Interface;
using System.Net;

namespace Publisher.Controllers.V1_0
{
    [Route("api/v1.0/comments")]
    [ApiController]
    public class CommentController(ICommentService commentService, ILogger<CommentController> logger) : Controller
    {

        [HttpGet]
        [Route("{id:int}")]
        public async Task<JsonResult> GetByID([FromRoute] int id)
        {
            CommentKafkaResponse? response = null;
            try
            {
                response = await commentService.GetByID(id);
            }
            catch (Exception ex)
            {
                logger.LogError($"Unable to deserialize CommentKafkaResponse {ex.Message}");
            }

            var statusCode =
                response is not null && response.State == State.Approve ? HttpStatusCode.OK : HttpStatusCode.NotFound;

            return new JsonResult(response?.ResponseTO)
            {
                StatusCode = (int)statusCode
            };
        }

        [HttpGet]
        public async Task<IActionResult> Read()
        {
            IList<CommentKafkaResponse>? response = null;
            try
            {
                response = await commentService.GetAll();
            }
            catch (Exception ex)
            {
                logger.LogError($"Unable to deserialize CommentKafkaResponse[] {ex.Message}");
            }
            

            bool okResult = false;
            if (response is not null)
            {
                if (response.Count != 0)
                {
                    okResult = response[0].State == State.Approve;
                }
                else
                {
                    okResult = true;
                }
            }

            var statusCode = okResult ? HttpStatusCode.OK : HttpStatusCode.BadRequest;

            return new JsonResult(response?.Select(r => r.ResponseTO).ToList())
            {
                StatusCode = (int)statusCode
            };
        }

        [HttpPost]
        public async Task<JsonResult> Create([FromBody] CommentRequestTO request)
        {
            CommentKafkaResponse? response = null;
            try
            {
                response = await commentService.Add(request);
            }
            catch (Exception ex)
            {
                logger.LogError($"Unable to deserialize CommentKafkaResponse {ex.Message}");
            }

            var statusCode =
                response is not null && response.State == State.Approve ? HttpStatusCode.Created : HttpStatusCode.NotFound;

            return new JsonResult(response?.ResponseTO)
            {
                StatusCode = (int)statusCode
            };
        }

        [HttpPut]
        public async Task<JsonResult> Update([FromBody] CommentRequestTO request)
        {
            CommentKafkaResponse? response = null;
            try
            {
                response = await commentService.Update(request);
            }
            catch (Exception ex)
            {
                logger.LogError($"Unable to deserialize CommentKafkaResponse {ex.Message}");
            }

            var statusCode =
                response is not null && response.State == State.Approve ? HttpStatusCode.OK : HttpStatusCode.NotFound;

            return new JsonResult(response?.ResponseTO)
            {
                StatusCode = (int)statusCode
            };
        }

        [HttpDelete]
        [Route("{id:int}")]
        public async Task<IActionResult> Delete([FromRoute] int id)
        {
            bool response = false;
            try
            {
                response = await commentService.Remove(id);
            }
            catch (Exception ex)
            {
                logger.LogError($"Unable to deserialize bool {ex.Message}");
            }

            var statusCode = response ? HttpStatusCode.NoContent : HttpStatusCode.BadRequest;

            return new StatusCodeResult((int)statusCode);
        }
    }
}
