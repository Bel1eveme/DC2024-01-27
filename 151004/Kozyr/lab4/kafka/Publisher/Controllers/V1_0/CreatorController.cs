using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using Publisher.Controllers.V1_0.Common;
using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Service.Interface;
using System.Net;

namespace Publisher.Controllers.V1_0
{
    [Route("api/v1.0/creators")]
    [ApiController]
    public class CreatorController(ICreatorService CreatorService, ILogger<CreatorController> Logger, IMapper Mapper) :
        AbstractController<Creator, CreatorRequestTO, CreatorResponseTO>(CreatorService, Logger, Mapper)
    {
        [HttpGet]
        [Route("issues/{id:int}")]
        public async Task<JsonResult> GetByIssueID([FromRoute] int id)
        {
            CreatorResponseTO? response = null;
            Logger.LogInformation("Getting creator by issue ID: {id}", id);

            try
            {
                response = await CreatorService.GetByIssueID(id);
            }
            catch (Exception ex)
            {
                Logger.LogError("Error getting creator by issue ID {ex}", ex);
                Response.StatusCode = (int)HttpStatusCode.BadRequest;
            }

            return Json(response);
        }
    }
}
