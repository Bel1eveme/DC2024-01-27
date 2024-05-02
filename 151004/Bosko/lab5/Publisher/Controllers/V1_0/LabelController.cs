using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using Publisher.Controllers.V1_0.Common;
using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Service.Interface;

namespace Publisher.Controllers.V1_0
{
    [Route("api/v1.0/labels")]
    [ApiController]
    public class LabelController(ILabelService LabelService, ILogger<LabelController> Logger, IMapper Mapper) :
        AbstractController<Label, LabelRequestTO, LabelResponseTO>(LabelService, Logger, Mapper)
    { }
}
