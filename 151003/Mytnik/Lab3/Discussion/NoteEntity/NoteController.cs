using AutoMapper;
using Discussion.Common;
using Discussion.NoteEntity.Interface;
using Microsoft.AspNetCore.Mvc;

namespace Discussion.NoteEntity
{
    [Route("api/v1.0/notes")]
    [ApiController]
    public class NoteController(INoteService NoteService, ILogger<NoteController> Logger, IMapper Mapper)
        : AbstractController<Note, NoteRequestTO, NoteResponseTO>(NoteService, Logger, Mapper)
    { }
}
