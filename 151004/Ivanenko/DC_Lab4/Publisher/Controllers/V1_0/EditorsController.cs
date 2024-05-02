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
    [Route("api/v1.0/editors")]
    [ApiController]
    public class EditorsController(IEditorService EditorService, ILogger<EditorsController> Logger, IMapper Mapper) :
        AbstractController<Editor, EditorRequestTO, EditorResponseTO>(EditorService, Logger, Mapper)
    {
        [HttpGet]
        [Route("tweets/{id:int}")]
        public async Task<JsonResult> GetByTweetID([FromRoute] int id)
        {
            EditorResponseTO? response = null;
            Logger.LogInformation("Getting Editor by tweet ID: {id}", id);

            try
            {
                response = await EditorService.GetByTweetID(id);
            }
            catch (Exception ex)
            {
                Logger.LogError("Error getting Editor by tweet ID {ex}", ex);
                Response.StatusCode = (int)HttpStatusCode.BadRequest;
            }

            return Json(response);
        }
    }
}
