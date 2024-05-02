using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.Services.Interfaces;

public interface ILabelService
{
	Task<IEnumerable<LabelResponseDto>> GetLabelsAsync();

	Task<LabelResponseDto> GetLabelByIdAsync(long id);

	Task<LabelResponseDto> CreateLabelAsync(LabelRequestDto label);

	Task<LabelResponseDto> UpdateLabelAsync(LabelRequestDto label);

	Task DeleteLabelAsync(long id);
}