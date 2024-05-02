using AutoMapper;
using Discussion.CommentEntity;

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
