using AutoMapper;
using Discussion.CommentEntity;
using Discussion.CommentEntity.Dto;

namespace Discussion.Common
{
    public class AutoMapper : Profile
    {
        public AutoMapper()
        {
            CreateMap<CommentRequestTO, Comment>();
            CreateMap<Comment, CommentResponseTO>();
        }
    }
}
