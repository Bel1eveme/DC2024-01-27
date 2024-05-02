using Microsoft.AspNetCore.Mvc;

namespace Forum.Api.Controllers;

[Route("health")]
[ApiController]
public class HealthCheckController : ControllerBase
{
    [HttpGet]
    public IActionResult Get()
    {
        return Ok("We are ok");
    }
}