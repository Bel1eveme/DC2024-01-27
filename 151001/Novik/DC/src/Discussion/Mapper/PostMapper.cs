using AutoMapper;
using Discussion.Models.DTOs.Requests;
using Discussion.Models.DTOs.Responses;
using Discussion.Models.Entity;

namespace Discussion.Mapper;

public class PostMapper : Profile
{

    public PostMapper()
    {
        CreateMap<Post, PostResponseTo>();
        CreateMap<PostRequestTo, Post>();
    }
    
}