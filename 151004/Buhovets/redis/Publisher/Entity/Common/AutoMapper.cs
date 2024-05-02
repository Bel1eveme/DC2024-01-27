using AutoMapper;
using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;

namespace Publisher.Entity.Common
{
    public class AutoMapper : Profile
    {
        public AutoMapper()
        {
            CreateMap<AuthorRequestTO, Author>();
            CreateMap<Author, AuthorResponseTO>();

            CreateMap<TagRequestTO, Tag>();
            CreateMap<Tag, TagResponseTO>();

            CreateMap<TweetRequestTO, Tweet>();
            CreateMap<Tweet, TweetResponseTO>();
        }
    }
}
