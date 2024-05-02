using Forum.Api.Models.Dto;

namespace Forum.Api.Services;

public interface IStoryService
{
    public Task<List<StoryResponseDto>> GetAllStories();

    public Task<StoryResponseDto?> GetStory(long id);
    
    public Task<StoryResponseDto> CreateStory(StoryRequestDto storyRequestDto); 
    
    public Task<StoryResponseDto?> UpdateStory(StoryRequestDto storyRequestDto); 
    
    public Task<StoryResponseDto?> DeleteStory(long id);
    
    public Task<bool> StoryExists(long id);
    
    public Task<CreatorResponseDto?> GetCreatorByStory(long id);
    
    public Task<List<TagResponseDto>> GetTagsByStory(long id);
    
    public Task<List<PostResponseDto>> GetPostsByStory(long id);

    public Task<List<StoryResponseDto>> GetStoriesWithFiltering(StorySearchDto searchDto);
}