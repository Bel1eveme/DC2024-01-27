using REST.Publisher.Models.DTOs.Request;
using REST.Publisher.Models.DTOs.Response;

namespace REST.Publisher.Services.Interfaces;

public interface INoteService: IService<NoteRequestDto, NoteResponseDto>
{
}