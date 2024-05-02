using Forum.Api.Models.Dto;

namespace Forum.Api.Services;

public interface ITagService
{
    public Task<List<TagResponseDto>> GetAllTags();

    public Task<TagResponseDto?> GetTag(long id);
    
    public Task<TagResponseDto> CreateTag(TagRequestDto tagRequestDto); 
    
    public Task<TagResponseDto?> UpdateTag(TagRequestDto tagRequestDto); 
    
    public Task<TagResponseDto?> DeleteTag(long id); 
}