using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using Publisher.Controllers.V1_0.Common;
using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Service.Interface;

namespace Publisher.Controllers.V1_0
{
    [Route("api/v1.0/issues")]
    [ApiController]
    public class IssueController(IIssueService IssueService, ILogger<IssueController> Logger, IMapper Mapper)
        : AbstractController<Issue, IssueRequestTO, IssueResponseTO>(IssueService, Logger, Mapper)
    { }
}
