using AutoMapper;
using Forum.Api.Models;
using Forum.Api.Models.Dto;

namespace Forum.Api;

public class AppMappingProfile : Profile
{
    public AppMappingProfile()
    {
        CreateMap<CreatorRequestDto, Creator>();
        CreateMap<Creator, CreatorResponseDto>();
        
        CreateMap<StoryRequestDto, Story>();
        CreateMap<Story, StoryResponseDto>();
        
        CreateMap<TagRequestDto, Tag>();
        CreateMap<Tag, TagResponseDto>();
        
        CreateMap<PostRequestDto, Post>();
        CreateMap<Post, PostResponseDto>();
    }
}