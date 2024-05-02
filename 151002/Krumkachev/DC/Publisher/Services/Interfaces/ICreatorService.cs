using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.Services.Interfaces;

public interface ICreatorService
{
	Task<IEnumerable<CreatorResponseDto>> GetCreatorsAsync();

	Task<CreatorResponseDto> GetCreatorByIdAsync(long id);

	Task<CreatorResponseDto> CreateCreatorAsync(CreatorRequestDto creator);

	Task<CreatorResponseDto> UpdateCreatorAsync(CreatorRequestDto creator);

	Task DeleteCreatorAsync(long id);
}