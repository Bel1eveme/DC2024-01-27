using System.ComponentModel.DataAnnotations;
using System.Net;
using Microsoft.AspNetCore.Diagnostics;
using LR1.Dto.Response;
using LR1.Exceptions;

namespace LR1.ExceptionHandlers;

public class GlobalExceptionHandler : IExceptionHandler
{
    public async ValueTask<bool> TryHandleAsync(HttpContext httpContext, Exception exception,
        CancellationToken cancellationToken)
    {
        var errorCode = exception switch
        {
            EntityNotFoundException => (int)HttpStatusCode.BadRequest + "01",
            ValidationException => (int)HttpStatusCode.BadRequest + "02",
            _ => (int)HttpStatusCode.InternalServerError + "00"
        };
        var response = new ErrorResponseTo(exception.Message, errorCode);
        httpContext.Response.StatusCode = Convert.ToInt32(errorCode.Substring(0, 3));
        await httpContext.Response.WriteAsJsonAsync(response, cancellationToken);
        return true;
    }
}