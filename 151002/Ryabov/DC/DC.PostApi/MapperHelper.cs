using Forum.PostApi.Models;
using Forum.PostApi.Models.Dto;

namespace Forum.PostApi;

public static class MapperHelper
{
    public static Post DtoToPost(PostRequestDto dto)
    {
        Post post = new()
        {
            Country = dto.Country,
            StoryId = dto.StoryId,
            Id = dto.Id,
            Content = dto.Content
        };

        return post;
    }
    
    public static PostResponseDto PostToDto(Post post)
    {
        PostResponseDto dto = new()
        {
            Country = post.Country,
            StoryId = post.StoryId,
            Id = post.Id,
            Content = post.Content
        };

        return dto;
    }
}