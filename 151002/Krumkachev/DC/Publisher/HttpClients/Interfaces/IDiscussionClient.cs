using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.HttpClients.Interfaces;

public interface IDiscussionClient
{
	Task<IEnumerable<PostResponseDto>> GetNotesAsync();

	Task<PostResponseDto> GetNoteByIdAsync(long id);

	Task<PostResponseDto> CreateNoteAsync(PostRequestDto post);

	Task<PostResponseDto> UpdateNoteAsync(PostRequestDto post);

	Task DeleteNoteAsync(long id);
}