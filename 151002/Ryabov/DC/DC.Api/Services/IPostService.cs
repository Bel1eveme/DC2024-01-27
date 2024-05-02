using Forum.Api.Models.Dto;

namespace Forum.Api.Services;

public interface IPostService
{
    public Task<IEnumerable<PostResponseDto>> GetAllPosts();

    public Task<PostResponseDto?> GetPost(long id);
    
    public Task<PostResponseDto> CreatePost(PostRequestDto postRequestDto); 
    
    public Task<PostResponseDto?> UpdatePost(PostRequestDto postRequestDto); 
    
    public Task<PostResponseDto?> DeletePost(long id); 
}