using AutoMapper;
using FluentValidation;
using Forum.Api.Models;
using Forum.Api.Models.Dto;
using Forum.Api.Repositories;
using ValidationException = System.ComponentModel.DataAnnotations.ValidationException;

namespace Forum.Api.Services;

public class CreatorService : ICreatorService
{
    private readonly ICreatorRepository _creatorRepository;

    private readonly IMapper _mapper;

    private readonly IValidator<CreatorRequestDto> _validator;

    public CreatorService(ICreatorRepository creatorRepository, IMapper mapper, IValidator<CreatorRequestDto> validator)
    {
        _creatorRepository = creatorRepository;
        _mapper = mapper;
        _validator = validator;
    }

    public async Task<List<CreatorResponseDto>> GetAllCreators()
    {
        var creators = await _creatorRepository.GetAllAsync();

        var creatorsResponseDto = _mapper.Map<List<CreatorResponseDto>>(creators);

        return creatorsResponseDto;
    }

    public async Task<CreatorResponseDto?> GetCreator(long id)
    {
        var creator = await _creatorRepository.GetByIdAsync(id);

        return creator is not null ? _mapper.Map<CreatorResponseDto>(creator) : null;
    }

    public async Task<CreatorResponseDto> CreateCreator(CreatorRequestDto creatorRequestDto)
    {
        var validationResult = await _validator.ValidateAsync(creatorRequestDto);

        if (!validationResult.IsValid)
        {
            throw new ValidationException(validationResult.Errors.FirstOrDefault()?.ErrorMessage);
        }
        
        var creatorModel = _mapper.Map<Creator>(creatorRequestDto);
        
        var creator = await _creatorRepository.CreateAsync(creatorModel);

        var creatorResponseDto = _mapper.Map<CreatorResponseDto>(creator);

        return creatorResponseDto;
    }

    public async Task<CreatorResponseDto?> UpdateCreator(CreatorRequestDto creatorRequestDto)
    {
        var creatorModel = _mapper.Map<Creator>(creatorRequestDto);
        
        var creator = await _creatorRepository.UpdateAsync(creatorModel.Id, creatorModel);

        return creator is not null ? _mapper.Map<CreatorResponseDto>(creator) : null;
    }

    public async Task<CreatorResponseDto?> DeleteCreator(long id)
    {
        var creator = await _creatorRepository.DeleteAsync(id);

        return creator is not null ? _mapper.Map<CreatorResponseDto>(creator) : null;
    }
}