using AutoMapper;
using FluentValidation;
using Forum.Api.Models;
using Forum.Api.Models.Dto;
using Forum.Api.Repositories;
using ValidationException = System.ComponentModel.DataAnnotations.ValidationException;

namespace Forum.Api.Services;

public class TagService : ITagService
{
    private readonly ITagRepository _tagRepository;

    private readonly IMapper _mapper;

    private readonly IValidator<TagRequestDto> _validator;

    public TagService(ITagRepository tagRepository, IMapper mapper, IValidator<TagRequestDto> validator)
    {
        _tagRepository = tagRepository;
        _mapper = mapper;
        _validator = validator;
    }

    public async Task<List<TagResponseDto>> GetAllTags()
    {
        var tags = await _tagRepository.GetAllAsync();

        var tagsResponseDto = _mapper.Map<List<TagResponseDto>>(tags);

        return tagsResponseDto;
    }

    public async Task<TagResponseDto?> GetTag(long id)
    {
        var tag = await _tagRepository.GetByIdAsync(id);

        return tag is not null ? _mapper.Map<TagResponseDto>(tag) : null;
    }

    public async Task<TagResponseDto> CreateTag(TagRequestDto tagRequestDto)
    {
        var validationResult = await _validator.ValidateAsync(tagRequestDto);

        if (!validationResult.IsValid)
        {
            throw new ValidationException(validationResult.Errors.FirstOrDefault()?.ErrorMessage);
        }
        
        var tagModel = _mapper.Map<Tag>(tagRequestDto);
        
        var tag = await _tagRepository.CreateAsync(tagModel);

        var tagResponseDto = _mapper.Map<TagResponseDto>(tag);

        return tagResponseDto;
    }

    public async Task<TagResponseDto?> UpdateTag(TagRequestDto storyRequestDto)
    {
        var tagModel = _mapper.Map<Tag>(storyRequestDto);
        
        var tag = await _tagRepository.UpdateAsync(tagModel.Id, tagModel);

        return tag is not null ? _mapper.Map<TagResponseDto>(tag) : null;
    }

    public async Task<TagResponseDto?> DeleteTag(long id)
    {
        var tag = await _tagRepository.DeleteAsync(id);

        return tag is not null ? _mapper.Map<TagResponseDto>(tag) : null;
    }
}