using Microsoft.AspNetCore.Mvc;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Kafka.Model;
using Publisher.Service.Interface;
using System.Net;

namespace Publisher.Controllers.V1_0
{
    [Route("api/v1.0/notes")]
    [ApiController]
    public class NoteController(INoteService noteService, ILogger<NoteController> logger) : Controller
    {

        [HttpGet]
        [Route("{id:int}")]
        public async Task<JsonResult> GetByID([FromRoute] int id)
        {
            NoteKafkaResponse? response = null;
            try
            {
                response = await noteService.GetByID(id);
            }
            catch (Exception ex)
            {
                logger.LogError($"Unable to deserialize NoteKafkaResponse {ex.Message}");
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
            IList<NoteKafkaResponse>? response = null;
            try
            {
                response = await noteService.GetAll();
            }
            catch (Exception ex)
            {
                logger.LogError($"Unable to deserialize NoteKafkaResponse[] {ex.Message}");
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
        public async Task<JsonResult> Create([FromBody] NoteRequestTO request)
        {
            NoteKafkaResponse? response = null;
            try
            {
                response = await noteService.Add(request);
            }
            catch (Exception ex)
            {
                logger.LogError($"Unable to deserialize NoteKafkaResponse {ex.Message}");
            }

            var statusCode =
                response is not null && response.State == State.Approve ? HttpStatusCode.Created : HttpStatusCode.NotFound;

            return new JsonResult(response?.ResponseTO)
            {
                StatusCode = (int)statusCode
            };
        }

        [HttpPut]
        public async Task<JsonResult> Update([FromBody] NoteRequestTO request)
        {
            NoteKafkaResponse? response = null;
            try
            {
                response = await noteService.Update(request);
            }
            catch (Exception ex)
            {
                logger.LogError($"Unable to deserialize NoteKafkaResponse {ex.Message}");
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
                response = await noteService.Remove(id);
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
