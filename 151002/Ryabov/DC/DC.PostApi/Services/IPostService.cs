using Forum.PostApi.Models;
using Forum.PostApi.Models.Dto;
using Forum.PostApi.Repositories.Base;

namespace Forum.PostApi.Services;

public interface IPostService
{
    public Task<IEnumerable<PostResponseDto>> GetAllPostsAsync();
    public Task<PostResponseDto?> GetPostAsync(long id);
    public Task<PostResponseDto?> CreatePostAsync(PostRequestDto postRequestDto);
    public Task<PostResponseDto?> UpdatePostAsync(PostRequestDto postRequestDto);
    public Task<PostResponseDto?> DeletePostAsync(long id);
}