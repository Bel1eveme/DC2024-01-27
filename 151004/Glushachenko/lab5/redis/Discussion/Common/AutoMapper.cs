using AutoMapper;
using Discussion.PostEntity;
using Discussion.PostEntity.Dto;

namespace Discussion.Common
{
    public class AutoMapper : Profile
    {
        public AutoMapper()
        {
            CreateMap<PostRequestTO, Post>();
            CreateMap<Post, PostResponseTO>();
        }
    }
}
