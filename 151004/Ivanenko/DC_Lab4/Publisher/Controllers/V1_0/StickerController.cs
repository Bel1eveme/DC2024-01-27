using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using Publisher.Controllers.V1_0.Common;
using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Service.Interface;

namespace Publisher.Controllers.V1_0
{
    [Route("api/v1.0/stickers")]
    [ApiController]
    public class StickerController(IStickerService StickerService, ILogger<StickerController> Logger, IMapper Mapper) :
        AbstractController<Sticker, StickerRequestTO, StickerResponseTO>(StickerService, Logger, Mapper)
    { }
}
