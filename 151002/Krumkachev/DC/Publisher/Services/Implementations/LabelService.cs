using AutoMapper;
using FluentValidation;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.Exceptions;
using Publisher.Infrastructure.Validators;
using Publisher.Models;
using Publisher.Repositories.Interfaces;
using Publisher.Services.Interfaces;

namespace Publisher.Services.Implementations;

public class LabelService(
	ILabelRepository labelRepository,
	IMapper mapper,
	LabelRequestDtoValidator validator) : ILabelService
{
	private readonly IMapper _mapper = mapper;
	private readonly ILabelRepository _labelRepository = labelRepository;
	private readonly LabelRequestDtoValidator _validator = validator;

	public async Task<IEnumerable<LabelResponseDto>> GetLabelsAsync()
	{
		var labels = await _labelRepository.GetAllAsync();
		return _mapper.Map<IEnumerable<LabelResponseDto>>(labels);
	}

	public async Task<LabelResponseDto> GetLabelByIdAsync(long id)
	{
		var label = await _labelRepository.GetByIdAsync(id)
					  ?? throw new NotFoundException(ErrorMessages.LabelNotFoundMessage(id));
		return _mapper.Map<LabelResponseDto>(label);
	}

	public async Task<LabelResponseDto> CreateLabelAsync(LabelRequestDto label)
	{
		_validator.ValidateAndThrow(label);
		var labelToCreate = _mapper.Map<Label>(label);
		var createdLabel = await _labelRepository.CreateAsync(labelToCreate);
		return _mapper.Map<LabelResponseDto>(createdLabel);
	}

	public async Task<LabelResponseDto> UpdateLabelAsync(LabelRequestDto label)
	{
		_validator.ValidateAndThrow(label);
		var labelToUpdate = _mapper.Map<Label>(label);
		var updatedLabel = await _labelRepository.UpdateAsync(labelToUpdate)
							 ?? throw new NotFoundException(ErrorMessages.LabelNotFoundMessage(label.Id));
		return _mapper.Map<LabelResponseDto>(updatedLabel);
	}

	public async Task DeleteLabelAsync(long id)
	{
		if (!await _labelRepository.DeleteAsync(id))
			throw new NotFoundException(ErrorMessages.LabelNotFoundMessage(id));
	}
}