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
    public class EditorController(IAuthorService AuthorService, ILogger<EditorController> Logger, IMapper Mapper) :
        AbstractController<Editor, EditorRequestTO, EditorResponseTO>(AuthorService, Logger, Mapper)
    {
        [HttpGet]
        [Route("tweets/{id:int}")]
        public async Task<JsonResult> GetByTweetID([FromRoute] int id)
        {
            EditorResponseTO? response = null;
            Logger.LogInformation("Getting author by tweet ID: {id}", id);

            try
            {
                response = await AuthorService.GetByTweetID(id);
            }
            catch (Exception ex)
            {
                Logger.LogError("Error getting author by tweet ID {ex}", ex);
                Response.StatusCode = (int)HttpStatusCode.BadRequest;
            }

            return Json(response);
        }
    }
}
