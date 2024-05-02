using Microsoft.AspNetCore.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using ValidationException = FluentValidation.ValidationException;

namespace Forum.Api.Controllers;

public class ExceptionController : ControllerBase
{
    private readonly ILogger<ExceptionController> _logger;
    public ExceptionController(ILogger<ExceptionController> logger)
    {
        _logger = logger;
    }
    
    [Route("/error")]
    [ApiExplorerSettings(IgnoreApi = true)]
    public IActionResult Error()
    {
        Exception? exception = HttpContext.Features.Get<IExceptionHandlerFeature>()?.Error;
        
        _logger.LogError("Error caught in global handler: {Message}", exception?.Message);
        
        if (exception is ValidationException)
        {
            return ValidationProblem(title: exception.Message, statusCode: 403);
        }
        
        if (exception is DbUpdateException)
        {
            return ValidationProblem(title: exception.Message, statusCode: 403);
        }

        return Problem(statusCode: 403);
    }
}