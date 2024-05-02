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
            CreateMap<EditorRequestTO, Editor>();
            CreateMap<Editor, EditorResponseTO>();

            CreateMap<StickerRequestTO, Sticker>();
            CreateMap<Sticker, StickerResponseTO>();

            CreateMap<PostRequestTO, Post>();
            CreateMap<Post, PostResponseTO>();

            CreateMap<TweetRequestTO, Tweet>();
            CreateMap<Tweet, TweetResponseTO>();
        }
    }
}
