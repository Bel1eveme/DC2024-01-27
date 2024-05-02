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
    [Route("api/v1.0/authors")]
    [ApiController]
    public class EditorController(IEditorService AuthorService,  IMapper Mapper) :
        AbstractController<Editor, EditorRequestTO, EditorResponseTO>(AuthorService, Mapper)
    {
        [HttpGet]
        [Route("tweets/{id:int}")]
        public async Task<JsonResult> GetByTweetID([FromRoute] int id)
        {
            EditorResponseTO? response = null;

            try
            {
                response = await AuthorService.GetByTweetID(id);
            }
            catch (Exception ex)
            {
                Response.StatusCode = (int)HttpStatusCode.BadRequest;
            }

            return Json(response);
        }
    }
}
