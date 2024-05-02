using AutoMapper;
using Microsoft.AspNetCore.Diagnostics;
using REST.Discussion.Models.DTOs.Response;

namespace REST.Discussion.Utilities.Exceptions.Handler;

public class GlobalExceptionHandler(IMapper mapper) : IExceptionHandler
{
    public async ValueTask<bool> TryHandleAsync(HttpContext httpContext, Exception exception,
        CancellationToken cancellationToken)
    {
        int code;
        ErrorResponseDto? errorMessage;
        switch (exception)
        {
            case ResourceNotFoundException notFoundException:
                code = 404;
                errorMessage = mapper.Map<ErrorResponseDto>(notFoundException);
                break;
            case ValidationException validationException:
                code = 400;
                errorMessage = mapper.Map<ErrorResponseDto>(validationException);
                break;
            case AssociationException associationException:
                code = 403;
                errorMessage = mapper.Map<ErrorResponseDto>(associationException);
                break;
            default:
                return false;
        }

        httpContext.Response.StatusCode = code;

        await httpContext.Response
            .WriteAsJsonAsync(errorMessage, cancellationToken);

        return true;
    }
}