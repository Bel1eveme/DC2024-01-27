using AutoMapper;
using Discussion.Common;
using Discussion.NoteEntity.Dto;
using Discussion.NoteEntity.Interface;
using Microsoft.AspNetCore.Mvc;
using System.Net;

namespace Discussion.NoteEntity
{
    [Route("api/v1.0/notes")]
    [ApiController]
    public class NoteController(INoteService NoteService, ILogger<NoteController> Logger, IMapper Mapper)
        : AbstractController<Note, NoteRequestTO, NoteResponseTO>(NoteService, Logger, Mapper)
    {
        [HttpGet]
        [Route("{id:int}")]
        public override async Task<JsonResult> GetByID([FromRoute] int id)
        {
            Logger.LogInformation("Get {type} {id}", typeof(Note), id);
            var json = Json(null);
            json.StatusCode = (int)HttpStatusCode.OK;

            try
            {
                var response = await NoteService.GetByID(id);
                json.Value = response;
            }
            catch
            {
                Logger.LogError("ERROR getting {type} {id}", typeof(Note), id);
                json.Value = new NoteResponseTO(default, default, string.Empty, string.Empty);
                json.StatusCode = (int)HttpStatusCode.NotFound;
            }

            return json;
        }
    }
}
