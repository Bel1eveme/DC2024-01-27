using AutoMapper;
using FluentValidation;
using Forum.Api.Models;
using Forum.Api.Models.Dto;
using Forum.Api.Repositories;
using Microsoft.EntityFrameworkCore;
using ValidationException = System.ComponentModel.DataAnnotations.ValidationException;

namespace Forum.Api.Services;

public class StoryService : IStoryService
{
    private readonly IStoryRepository _storyRepository;

    private readonly ICreatorService _creatorService;
    private readonly ITagService _tagService;
    private readonly IPostService _postService;

    private readonly IMapper _mapper;
    private readonly IValidator<StoryRequestDto> _storyValidator;
    private readonly IValidator<StorySearchDto> _storySearchValidator;
    

    public StoryService(IStoryRepository storyRepository, IMapper mapper,
        IValidator<StoryRequestDto> storyValidator, ICreatorService creatorService,
        ITagService tagService, IPostService postService, IValidator<StorySearchDto> storySearchValidator)
    {
        _storyRepository = storyRepository;
        _mapper = mapper;
        _storyValidator = storyValidator;
        _creatorService = creatorService;
        _tagService = tagService;
        _postService = postService;
        _storySearchValidator = storySearchValidator;
    }

    public async Task<List<StoryResponseDto>> GetAllStories()
    {
        var stories = await _storyRepository.GetAllAsync();

        var storiesResponseDto = _mapper.Map<List<StoryResponseDto>>(stories);

        return storiesResponseDto;
    }

    public async Task<StoryResponseDto?> GetStory(long id)
    {
        var story = await _storyRepository.GetByIdAsync(id);

        return story is not null ? _mapper.Map<StoryResponseDto>(story) : null;
    }

    public async Task<StoryResponseDto> CreateStory(StoryRequestDto storyRequestDto)
    {
        var validationResult = await _storyValidator.ValidateAsync(storyRequestDto);

        if (!validationResult.IsValid)
        {
            throw new ValidationException(validationResult.Errors.FirstOrDefault()?.ErrorMessage);
        }
        
        var storyModel = _mapper.Map<Story>(storyRequestDto);
        
        var story = await _storyRepository.CreateAsync(storyModel);

        var storyResponseDto = _mapper.Map<StoryResponseDto>(story);

        return storyResponseDto;
    }

    public async Task<StoryResponseDto?> UpdateStory(StoryRequestDto storyRequestDto)
    {
        var storyModel = _mapper.Map<Story>(storyRequestDto);
        
        var story = await _storyRepository.UpdateAsync(storyModel.Id, storyModel);

        return story is not null ? _mapper.Map<StoryResponseDto>(story) : null;
    }

    public async Task<StoryResponseDto?> DeleteStory(long id)
    {
        var story = await _storyRepository.DeleteAsync(id);

        return story is not null ? _mapper.Map<StoryResponseDto>(story) : null;
    }
    
    public async Task<bool> StoryExists(long id)
    {
        return await _storyRepository.GetByIdAsync(id) is not null;
    }

    public async Task<List<StoryResponseDto>> GetStoriesWithFiltering(StorySearchDto searchDto)
    {
        var validationResult = await _storySearchValidator.ValidateAsync(searchDto);

        if (!validationResult.IsValid)
        {
            throw new ValidationException(validationResult.Errors.FirstOrDefault()?.ErrorMessage);
        }
        
        var stories = _storyRepository.GetAllWithFilteringAsync();

        if (searchDto.Content is not null)
        {
            stories = stories.Where(x => x.Content == searchDto.Content);
        }
        
        if (searchDto.Title is not null)
        {
            stories = stories.Where(x => x.Title == searchDto.Title);
        }
        
        if (searchDto.CreatorLogin is not null)
        {
            stories = stories.Where(x => x.Creator.Login == searchDto.CreatorLogin);
        }
        
        if (searchDto.TagIds.Count > 0)
        {
            stories = stories.Where(story => searchDto.TagIds.All(t => story.Tags.Any(storyTag => storyTag.Id == t)));
        }
        
        if (searchDto.TagNames.Count > 0)
        {
            stories = stories.Where(story => searchDto.TagNames.All(t => story.Tags.Any(storyTag => storyTag.Name == t)));
        }
        
        return _mapper.Map<List<StoryResponseDto>>(await stories.ToListAsync());
    }
    
    public async Task<CreatorResponseDto?> GetCreatorByStory(long id)
    {
        return await _creatorService.GetCreator(id);
    }

    public async Task<List<TagResponseDto>> GetTagsByStory(long id)
    {
        return (await _tagService.GetAllTags()).Where(t => t.Stories.All(s => s.Id == id)).ToList();
    }

    public async Task<List<PostResponseDto>> GetPostsByStory(long id)
    {
        return (await _postService.GetAllPosts()).Where(p => p.StoryId == id).ToList();
    }
}