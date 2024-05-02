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
            CreateMap<UserRequestTO, User>();
            CreateMap<User, UserResponseTO>();

            CreateMap<LabelRequestTO, Label>();
            CreateMap<Label, LabelResponseTO>();

            CreateMap<NoteRequestTO, Note>();
            CreateMap<Note, NoteResponseTO>();

            CreateMap<NewsRequestTO, News>();
            CreateMap<News, NewsResponseTO>();
        }
    }
}
