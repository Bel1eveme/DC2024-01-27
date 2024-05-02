using Forum.Api.Models.Dto;

namespace Forum.Api.Services;

public interface ICreatorService
{
    public Task<List<CreatorResponseDto>> GetAllCreators();

    public Task<CreatorResponseDto?> GetCreator(long id);
    
    public Task<CreatorResponseDto> CreateCreator(CreatorRequestDto creatorRequestDto); 
    
    public Task<CreatorResponseDto?> UpdateCreator(CreatorRequestDto creatorRequestDto); 
    
    public Task<CreatorResponseDto?> DeleteCreator(long id); 
}